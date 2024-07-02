package com.nomorelateness.donotlate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.gson.Gson
import com.nomorelateness.donotlate.databinding.ActivityMainBinding
import com.nomorelateness.donotlate.feature.auth.presentation.view.LoginFragment
import com.nomorelateness.donotlate.feature.main.presentation.view.MainFragment
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.MyPromiseListFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<com.nomorelateness.donotlate.MainViewModel> {
        val appContainer =
            (application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        com.nomorelateness.donotlate.MainViewModelFactory(
            sessionManager = appContainer.sessionManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.nomorelateness.donotlate.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        collectFlows()

        handleIntent(intent)
    }

    private fun collectFlows() {
        lifecycleScope.launch {
            repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.channel.collect {
                    when (it) {
                        com.nomorelateness.donotlate.MainAction.LoggedIn -> navigateToMainScreen()
                        com.nomorelateness.donotlate.MainAction.NotLoggedIn -> navigateToLoginScreen()
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
        val fragment = MyPromiseListFragment()

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
            )
            .replace(R.id.frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToMainScreen() {
        supportFragmentManager.beginTransaction()
            .add(com.nomorelateness.donotlate.R.id.frame, MainFragment())
            .commit()
    }

    private fun navigateToLoginScreen() {
        supportFragmentManager.beginTransaction()
            .add(com.nomorelateness.donotlate.R.id.frame, LoginFragment())
            .commit()
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(com.nomorelateness.donotlate.R.id.frame, fragment)
            .commit()
    }

    fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(com.nomorelateness.donotlate.R.id.frame, fragment)
            .commit()
    }
}