package com.example.donotlate.consumption.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.ActivityCalculationBinding

class CalculationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val binding by lazy { ActivityCalculationBinding.inflate(layoutInflater) }

    private val viewModel: SharedViewModel by viewModels {
        val appContainer = (application as DoNotLateApplication).appContainer
        SharedViewModelFactory(
            appContainer.consumptionRepository,
//            appContainer.getFinishedConsumptionUseCase,
//            appContainer.getUnfinishedConsumptionUseCase,
//            appContainer.insertConsumptionUseCase
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.calculation)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }

}