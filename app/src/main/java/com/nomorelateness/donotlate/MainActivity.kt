package com.nomorelateness.donotlate

import android.app.Service
import android.content.Context
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
import com.nomorelateness.donotlate.databinding.ActivityMainBinding
import com.nomorelateness.donotlate.feature.auth.presentation.view.LoginFragment
import com.nomorelateness.donotlate.feature.main.presentation.view.MainFragment
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
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.nomorelateness.donotlate.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        loadingInit()
        collectFlows()

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

    //앱 실행 시 메인프래그먼트 로딩
    private fun loadingInit(){
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString(getString(com.nomorelateness.donotlate.R.string.preference_loading_key), "1")
            apply()
        }
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

    override fun onDestroy() {
        super.onDestroy()

        Log.d("메인 엑티비티","onDestroy()")
    }

    override fun finish() {
        super.finish()

        Log.d("메인 엑티비티","finish()")
    }
}