package com.nomorelateness.donotlate

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.nomorelateness.donotlate.databinding.ActivityMainBinding
import com.nomorelateness.donotlate.feature.auth.presentation.view.LoginFragment
import com.nomorelateness.donotlate.feature.main.presentation.view.MainFragment
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.MyPromiseRoomFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<com.nomorelateness.donotlate.MainViewModel> {
        val appContainer =
            (application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        com.nomorelateness.donotlate.MainViewModelFactory(
            sessionManager = appContainer.sessionManager,
            checkUserEmailVerificationUseCase = appContainer.checkUserEmailVerificationUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.nomorelateness.donotlate.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingInit()
        collectFlows()
        initDarMode()

//        handleIntent(intent)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.channel.collect {
                    when (it) {
                        MainAction.LoggedIn -> navigateToMainScreen()
                        MainAction.NotLoggedIn -> navigateToLoginScreen()
                        MainAction.EmailNotVerified -> navigateToLoginScreen()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun isIntentNull(intent: Intent): Boolean {
        val promiseJson = intent.getStringExtra("promiseRoom")
        return promiseJson == null
    }

    private fun handleIntent(intent: Intent) {
        val promiseJson = intent.getStringExtra("promiseRoom")
        if (promiseJson != null) {
            Log.d("확인 인텐트", "${promiseJson}")
            val promiseModel = Gson().fromJson(promiseJson, PromiseModel::class.java)
            openPromiseRoomFragment(promiseModel)
            // Intent 데이터를 초기화하여 다시 앱이 열릴 때 자동으로 이동하지 않게 함
            intent.removeExtra("promiseRoom")
        }
    }

    private fun openPromiseRoomFragment(roomInfo: PromiseModel) {
        val fragment = MyPromiseRoomFragment()
        val bundle = Bundle()
        Log.d("확인 intent 전체", "${roomInfo}")
        bundle.putParcelable("promiseRoom", roomInfo)
        bundle.putBoolean("isWidget", true)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
            )
            //add로 하기
            .add(R.id.frame, fragment)
            .addToBackStack("myPromiseRoomFragment")
            .commit()
    }

    //TODO 다른 프래그먼트, 액티비티 위에서 위젯 클릭 시의 동작 처리하기 (현재 uid 못받아옴)
//    override fun onResume() {
//        super.onResume()
//        Log.d("확인 onresume", "rejoiwa;jfoiajf")
////        supportFragmentManager.beginTransaction()
////            .add(com.nomorelateness.donotlate.R.id.frame, MainFragment())
////            .commit()
//        if (!isIntentNull(intent)) {
//            //null
//            Handler(Looper.getMainLooper()).postDelayed({
//                handleIntent(intent)
//            }, 1000)
//        }
//    }

    private fun navigateToMainScreen() {

        supportFragmentManager.beginTransaction()
            .add(com.nomorelateness.donotlate.R.id.frame, MainFragment())
            .commit()

        if (!isIntentNull(intent)) {
            //null
            Handler(Looper.getMainLooper()).postDelayed({
                handleIntent(intent)
            }, 1000)
        }
    }

    private fun navigateToLoginScreen() {
        supportFragmentManager.beginTransaction()
            .add(com.nomorelateness.donotlate.R.id.frame, LoginFragment())
            .commit()
    }

    //앱 실행 시 메인프래그먼트 로딩
    private fun loadingInit() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(com.nomorelateness.donotlate.R.string.preference_loading_key), "1")
            apply()
        }
    }

    private fun initDarMode() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return

        val sharedPrefValue = resources.getString(R.string.preference_darkMode_key)
        val darkModeValue =
            sharedPref.getString(getString(R.string.preference_darkMode_key), sharedPrefValue)

        if (darkModeValue == "darkModeOn") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}