package com.nomorelateness.donotlate

import android.content.pm.ActivityInfo
import android.os.Bundle
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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(com.nomorelateness.donotlate.R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
}