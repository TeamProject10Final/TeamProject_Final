package com.example.donotlate

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
import com.example.donotlate.databinding.ActivityMainBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.main.presentation.view.MainFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<MainViewModel> {
        val appContainer = (application as DoNotLateApplication).appContainer
        MainViewModelFactory(
            sessionManager = appContainer.sessionManager
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
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
                    }
                }
            }
        }
    }

    private fun navigateToMainScreen() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, MainFragment())
            .commit()
    }

    private fun navigateToLoginScreen() {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, LoginFragment())
            .commit()
    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.frame, fragment)
            .commit()
    }

    fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
            .commit()
    }
}