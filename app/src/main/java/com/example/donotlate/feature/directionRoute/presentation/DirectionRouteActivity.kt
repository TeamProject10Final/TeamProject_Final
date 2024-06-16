package com.example.donotlate.feature.directionRoute.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.ActivityDirectionRouteBinding
import com.example.donotlate.databinding.ActivityMainBinding

class DirectionRouteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDirectionRouteBinding


    val sharedViewModel: DirectionsViewModel1 by lazy {
        val appContainer = (application as DoNotLateApplication).appContainer
        val directions1Container = appContainer.directions1Container
        val directionsViewModel1Factory = directions1Container.directionsViewModel1Factory
        directionsViewModel1Factory?.let {
            ViewModelProvider(this, it).get(DirectionsViewModel1::class.java)
        } ?: throw IllegalStateException("DirectionsViewModel1Factory not initialized properly")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectionRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.directionRouteActivity)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val delayFragment = DelayFragment()
        var currentUserLocation = intent.getStringExtra("userLocation")
        var destination = intent.getStringExtra("destination")
        val args = Bundle().apply {
            putString("currentUserlocation", currentUserLocation)
            putString("destination", destination)
        }
        delayFragment.arguments = args

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, delayFragment)
                .commit()
        }

    }
}