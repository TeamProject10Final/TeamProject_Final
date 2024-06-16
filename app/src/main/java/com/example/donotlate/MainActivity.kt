package com.example.donotlate

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.donotlate.databinding.ActivityMainBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment

class MainActivity : AppCompatActivity() {


    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            changeFragment(MainFragment())
        }


//        lifecycleScope.launch {
//            NetWorkClient.googleNetWork.requestDestination(
//                query = "restaurants%20in%20Sydney",
//                radius = 1500,
//                language = "ko"
//            )
//        }장소 통신 완

    }

    fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, fragment)
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