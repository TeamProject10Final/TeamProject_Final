package com.example.donotlate.feature.consumption.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.donotlate.R
import com.example.donotlate.databinding.ActivityCalculationBinding

class CalculationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val binding by lazy { ActivityCalculationBinding.inflate(layoutInflater) }

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


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // 현재 프래그먼트의 뒤로가기 이벤트를 처리
        val handled = navController.navigateUp()

        // 현재 프래그먼트가 없거나, 루트 프래그먼트라면 액티비티의 뒤로가기 이벤트 처리...
        if (!handled) {
            super.onBackPressed()
        }
    }

}