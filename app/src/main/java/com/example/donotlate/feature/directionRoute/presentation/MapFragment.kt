package com.example.donotlate.feature.directionRoute.presentation

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.donotlate.AppContainer
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    private var isUserInteracting = false

    lateinit var currentUserLocation: String
    lateinit var currentDestination: String

    private val handler = Handler(Looper.getMainLooper())
    private var checkBoundsRunnable: Runnable? = null


    private lateinit var locationPermission: ActivityResultLauncher<Array<String>>
//    private lateinit var locationCallback: LocationCallback
//    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val appContainer: AppContainer by lazy {
        (requireActivity().application as DoNotLateApplication).appContainer
    }

    // DirectionsViewModel1Factory 가져오기
    private val directionsViewModel1Factory: DirectionsViewModel1Factory by lazy {
        appContainer.directions1Container.directionsViewModel1Factory
    }

    // SharedViewModel 가져오기
    private val sharedViewModel: DirectionsViewModel1 by activityViewModels { directionsViewModel1Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (results.all { it.value }) {
                initializeMapView()
            } else {
                Toast.makeText(requireContext(), "권한 승인이 필요합니다.", Toast.LENGTH_LONG).show()
            }
        }

        requestLocationPermission()
//
//        arguments?.let {
//            currentUserLocation = it.getString("currenUserLocation").toString()
//            currentDestination = it.getString("destination").toString()
//        }
//
//        searchDirections()

    }

//    private fun searchDirections() {
//        var origin = currentDestination
//        if(origin!=null){
//            var destination = currentDestination
//            val mode = "transit"
//            sharedViewModel.getDirections(origin, destination, mode)
//        }
//    }

    private fun requestLocationPermission() {
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMapView()
        setupClickListener()

        binding.btnMapBottomSheet.setOnClickListener {
            val bottomSheetDialogFragment = RouteDetailsBottomSheet()
            bottomSheetDialogFragment.show(parentFragmentManager, "tag")
        }
        sharedViewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
        sharedViewModel.latLngBounds.observe(viewLifecycleOwner) { list ->
            Log.d("확인","latLngBounds ${list}")
            // 경로를 포함하는 영역 계산하여 지도의 중심을 이동
            if (list.isEmpty()) {
                return@observe
            }
            val latLngBounds = LatLngBounds.builder()
            list.forEach {
                latLngBounds.include(LatLng(it.lat, it.lng))
            }
            val bounds = latLngBounds.build()

            val padding = 100
            //googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
    }

    private fun setupClickListener() {
        binding.btnMapBottomSheet.setOnClickListener {
            val bottomSheetDialogFragment = RouteDetailsBottomSheet()
            bottomSheetDialogFragment.show(parentFragmentManager, "tag")
        }

    }

    private fun setupMapListeners() {
        googleMap?.setOnCameraMoveStartedListener { reason ->
            if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                isUserInteracting = true
                // 사용자가 지도를 움직이기 시작하면 기존의 Runnable 제거
                checkBoundsRunnable?.let { handler.removeCallbacks(it) }
            }
        }

        googleMap?.setOnCameraIdleListener {
            if (isUserInteracting) {
                isUserInteracting = false

                checkBoundsRunnable = Runnable {
                    val currentBounds = googleMap!!.projection.visibleRegion.latLngBounds

                    // 경로를 포함하는 영역 계산하여 지도의 중심을 이동
                    val latLngBounds = LatLngBounds.builder()
                    sharedViewModel.latLngBounds.value?.forEach {
                        latLngBounds.include(LatLng(it.lat, it.lng))
                    }

                    val routeBounds = latLngBounds.build()
                    if (!currentBounds.contains(routeBounds.northeast) || !currentBounds.contains(
                            routeBounds.southwest
                        )
                    ) {
                        //focusMapOnBounds()
                    }
                }
                handler.postDelayed(checkBoundsRunnable!!, 3000)  // 3초 지연
            }
        }
    }

    private fun initializeMapView() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }


    override fun onResume() {
        super.onResume()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.onResume()
    }

    override fun onPause() {
        super.onPause()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.onDestroyView()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
        mapFragment?.onLowMemory()
    }

    private fun setMarker(myMap: GoogleMap) {
        googleMap = myMap
        Log.d("라인확인", "onmapready")
        val markerOrigin = MarkerOptions().position(sharedViewModel.getOrigin()).title("출발지")
        val markerDestination =
            MarkerOptions().position(sharedViewModel.getDestination()).title("목적지")

        myMap.addMarker(markerOrigin)
        myMap.addMarker(markerDestination)

        //focusMapOnBounds()
    }

    private fun focusMapOnBounds() {
//        // 경로를 포함하는 영역 계산하여 지도의 중심을 이동
//        val latLngBounds = LatLngBounds.builder()
//        sharedViewModel.latLngBounds.value?.forEach {
//            latLngBounds.include(LatLng(it.lat, it.lng))
//        }
//        val bounds = latLngBounds.build()
//
//        if (bounds.southwest != null && bounds.northeast != null) {
//            val padding = 100
//            googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
//        } else {
//            Log.e("확인 mapf", "LatLngBounds 실패")
//        }
    }

    private fun setLine(myMap: GoogleMap) {
        googleMap = myMap
        sharedViewModel.polyLine.observe(viewLifecycleOwner, Observer { polylines ->

            polylines.forEach { polyline ->
                googleMap?.addPolyline(polyline)
            }

        })
    }

    override fun onMapReady(myMap: GoogleMap) {
        Log.d("확인","onMapReady ${myMap}")
        googleMap = myMap
//        setLine(myMap)
//        setMarker(myMap)
//
//        setupMapListeners()
    }


}