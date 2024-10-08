package com.nomorelateness.donotlate.feature.directionRoute.presentation

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.ActivityDirectionRouteBinding

class DirectionRouteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDirectionRouteBinding

    val sharedViewModel: DirectionsViewModel1 by lazy {
        val appContainer =
            (application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        val directions1Container = appContainer.directions1Container
        val directionsViewModel1Factory = directions1Container.directionsViewModel1Factory
        directionsViewModel1Factory?.let {
            ViewModelProvider(this, it).get(DirectionsViewModel1::class.java)
        } ?: throw IllegalStateException("DirectionsViewModel1Factory not initialized properly")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
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
        val currentDestLat = intent.getStringExtra("des lat").toString().toDouble()
        val currentDestLng = intent.getStringExtra("des lng").toString().toDouble()


        val args = Bundle().apply {
            putString("currentUserlocation", currentUserLocation)
            putString("destination", destination)
            putDouble("des lat", currentDestLat)
            putDouble("des lng", currentDestLng)
        }
        delayFragment.arguments = args

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, delayFragment)
                .commit()
        }

    }
}