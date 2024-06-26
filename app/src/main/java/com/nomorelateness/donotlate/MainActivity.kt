package com.nomorelateness.donotlate

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