package com.example.donotlate.feature.directionRoute.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.AppContainer
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMapBinding
import com.example.donotlate.feature.room.presentation.dialog.RoomTimeDialog
import com.example.donotlate.feature.room.presentation.dialog.TimePickerInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class MapFragment : Fragment(), OnMapReadyCallback, TimePickerInterface {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    private var isUserInteracting = false

    private val handler = Handler(Looper.getMainLooper())
    private var checkBoundsRunnable: Runnable? = null

    private var mAmpm = 0
    private var mMinute = 0
    private var mHour = 0


    private lateinit var locationPermission: ActivityResultLauncher<Array<String>>

    //private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient

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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkPermission()
        //getCurrentLocation()
        return binding.root
    }

    private fun checkPermission() {
        if (hasLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeMapView()
        setupClickListener()
        setUpSpinners()
        setUpTimePicker()

        observeViewModels()


    }

    private fun observeViewModels() {
        sharedViewModel.latLngBounds.observe(viewLifecycleOwner) { list ->
            Log.d("확인", "latLngBounds $list")
            // 경로를 포함하는 영역 계산하여 지도의 중심을 이동

            if (list.isNullOrEmpty()) {
                return@observe
            }
//            //여기에서 터짐.... transit 선택 후 1번 인덱스 선택 시
//            if ((list?.isEmpty() != false) as Boolean) {
//                return@observe
//            }
            val latLngBounds = LatLngBounds.builder()
            list.forEach {
                latLngBounds.include(LatLng(it.lat, it.lng))
            }
            val bounds = latLngBounds.build()

            val padding = 100
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
//
        sharedViewModel.mode.observe(viewLifecycleOwner) { mode ->
            Log.d("확인 mode2", "$mode")
            when (mode) {
                "transit" -> {
                    if (sharedViewModel.getCountry() == null) {
                        return@observe
                    }
                    binding.spinner2tm.visibility = View.VISIBLE
                    binding.spinner3rp.visibility = View.VISIBLE
                    binding.btnSelectTime.visibility = View.VISIBLE
                    sharedViewModel.setIsDepArrNone(0)
                    updateTimeButton(0)
                    binding.etTime.visibility = View.VISIBLE
                    binding.etTime.isVisible = false
                    binding.btnSearchDirectionRoutes.visibility = View.VISIBLE
                    binding.btnMapBottomSheet.visibility = View.VISIBLE
                }

                "select" -> {
                    //
                }

                else -> {

                    if (sharedViewModel.getCountry() == "대한민국" || sharedViewModel.getCountry() == "South Korea") {
                        binding.spinner2tm.visibility = View.GONE
                        binding.spinner3rp.visibility = View.GONE
                        binding.btnSelectTime.visibility = View.GONE
                        binding.etTime.visibility = View.GONE
                        binding.btnSearchDirectionRoutes.visibility = View.GONE
                        binding.ivDetailView.isVisible = true
                        binding.btnMapBottomSheet.visibility = View.GONE
                        sharedViewModel.checkAvailable()
                        Log.d("확인 도보", "ㅇㅇㄹㄷㄷ머ㅑ래더")
                        return@observe
                    }

                    lifecycleScope.launch {
                        binding.spinner2tm.visibility = View.GONE
                        binding.spinner3rp.visibility = View.GONE
                        binding.btnSelectTime.visibility = View.GONE
                        binding.etTime.visibility = View.GONE
                        binding.btnSearchDirectionRoutes.visibility = View.GONE
                        binding.ivDetailView.isVisible = false
                        Log.d("확인 user map", sharedViewModel.getUserLocationString().toString())

                        withContext(Dispatchers.IO) {
                            //여기서 바로 검색하게 하기 - 자동차, 도보 등
                            sharedViewModel.checkAvailable()
                            sharedViewModel.getDirections()
                        }
                        delay(1000)

                        sharedViewModel.afterSelecting()
                        val bottomSheet = DirectionsDialogFragment()
                        bottomSheet.show(parentFragmentManager, "tag")
                        sharedViewModel.refreshIndex()
                        binding.btnMapBottomSheet.visibility = View.VISIBLE
                    }
                }
            }
        }
        sharedViewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            Log.d("확인 error ob", it)
        }

        sharedViewModel.selectedTime.observe(viewLifecycleOwner) { time ->
            time?.let {
                val result01 = StringBuilder()
                if (it.hour > 12) {
                    result01.append("오후 ")
                    val selectedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", it.hour - 12, it.minute)
                    result01.append(selectedTime)
//                    binding.etTime.setText(result01)
                } else {
                    result01.append("오전 ")
                    val selectedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", it.hour, it.minute)
                    result01.append(selectedTime)
//                    binding.etTime.setText(result01)
                }
                binding.etTime.setText(result01)

            }
        }

        sharedViewModel.isDepArrNone.observe(viewLifecycleOwner) { isDep ->
            updateTimeButton(isDep)
        }

//        sharedViewModel.selectedRouteIndex.observe(viewLifecycleOwner) {
//            sharedViewModel.afterSelecting()
//        }

        sharedViewModel.startLocation.observe(viewLifecycleOwner) {
            Log.d("확인 마커", "$googleMap")
            val markerOrigin = MarkerOptions().position(it).title("출발지")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            val markerDestination =
                MarkerOptions().position(sharedViewModel.getDestination()).title("목적지")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))

            googleMap?.addMarker(markerOrigin)
            googleMap?.addMarker(markerDestination)
        }
    }

    private fun setUpTimePicker() {

        binding.etTime.setOnClickListener {
            showTimePickerDialog()
        }
    }

    private fun showTimePickerDialog() {

        val dialog = RoomTimeDialog(this, mAmpm, mHour, mMinute)
        dialog.show(childFragmentManager, "tag")

    //        val currentTime = Calendar.getInstance()
//        val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
//        val currentMinute = currentTime.get(Calendar.MINUTE)
//
//        TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
//            val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute)
//            binding.etTime.setText(selectedTime)
//
//            sharedViewModel.setTime(hourOfDay, minute)
//
//        }, currentHour, currentMinute, true).show()
    }

    private fun setUpSpinners() {
        val modeKeyArray = resources.getStringArray(R.array.modeKeyArray)
        val modeArray = resources.getStringArray(R.array.modeArray)
        val list1 = mutableListOf<FirstMode>()
        for (i in modeArray.indices) {
            list1.add(FirstMode(FirstModeEnum.entries[i], modeKeyArray[i], modeArray[i]))
        }
        val adapterMode = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            modeArray
        ) {
            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        adapterMode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner1mode.adapter = adapterMode
        binding.spinner1mode.setSelection(modeArray.size - 1)
        //

        val trafficModeKeyArray = resources.getStringArray(R.array.trafficModeKeyArray)
        val trafficModeArray = resources.getStringArray(R.array.trafficModeArray)
        val list2 = mutableListOf<TransitMode>()
        for (i in trafficModeArray.indices) {
            list2.add(
                TransitMode(
                    TransitModeEnum.entries[i],
                    trafficModeKeyArray[i],
                    trafficModeArray[i]
                )
            )
        }

        val adapterTm = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            trafficModeArray
        ) {
            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        adapterTm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner2tm.adapter = adapterTm
        binding.spinner2tm.setSelection(trafficModeArray.size - 1)
        //

        //
        val routingPreferenceKeyArray =
            resources.getStringArray(R.array.transitRoutingPreferenceKeyArray)
        val routingPreferenceArray = resources.getStringArray(R.array.transitRoutingPreferenceArray)
        val list3 = mutableListOf<TransitRoutePreference>()
        for (i in routingPreferenceArray.indices) {
            list3.add(
                TransitRoutePreference(
                    TransitRoutePreferenceEnum.entries[i],
                    routingPreferenceKeyArray[i],
                    routingPreferenceArray[i]
                )
            )
        }

        val adapterRp = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            routingPreferenceArray
        ) {
            override fun getCount(): Int {
                return super.getCount() - 1
            }
        }
        adapterRp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner3rp.adapter = adapterRp
        binding.spinner3rp.setSelection(adapterRp.count)


        binding.spinner1mode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedViewModel.refreshIndex()
                val selectedMode = list1[position]
                Log.d("확인 mode", "$selectedMode")
                sharedViewModel.setMode(selectedMode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spinner2tm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedViewModel.refreshIndex()
                val selectedMode = list2[position]
                sharedViewModel.setTransitMode(selectedMode)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.spinner3rp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sharedViewModel.refreshIndex()
                val selectedItem = list3[position]
                sharedViewModel.setRoutingPreference(selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun setupClickListener() {
        binding.btnMapBottomSheet.setOnClickListener {
            val bottomSheetDialogFragment = RouteDetailsBottomSheet()
            bottomSheetDialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
            bottomSheetDialogFragment.show(parentFragmentManager, "tag")
        }
//        binding.btnSendIndex.setOnClickListener {
//
//            val selectedIndex = binding.etSelectionIndex.text.toString()
//            if (selectedIndex != "") {
//                val thisIndex = selectedIndex.toInt()
//                sharedViewModel.setSelectedRouteIndex(thisIndex)
//                if (sharedViewModel.mode.value.toString() == "transit") {
//                    Log.d("확인 화살표", "${sharedViewModel.mode.value.toString()}")
//                    binding.ivDetailView.isVisible = true
//                } else {
//                    Log.d("확인 화살표 else", "${sharedViewModel.mode.value.toString()}")
//                    binding.ivDetailView.isVisible = false
//                }
//            } else {
//                Log.d("확인 인덱스 오류", "$selectedIndex")
//            }
//        }

        binding.btnSelectTime.setOnClickListener {
            sharedViewModel.changeIsDepArrNone()
        }

        //검색버튼
        binding.btnSearchDirectionRoutes.setOnClickListener {
            Log.d("확인 다른검색", sharedViewModel.routingPreference.value.toString())
            lifecycleScope.launch {
                binding.spinner2tm.visibility = View.GONE
                binding.spinner3rp.visibility = View.GONE
                binding.btnSelectTime.visibility = View.GONE
                binding.etTime.visibility = View.GONE
                binding.btnSearchDirectionRoutes.visibility = View.GONE

                withContext(Dispatchers.IO) {
                    //sharedViewModel.checkAvailable()
                    sharedViewModel.getDirByTransit()
                }
                delay(1000)
                val bottomSheet = DirectionsDialogFragment()
                bottomSheet.show(parentFragmentManager, "tag")

                binding.btnMapBottomSheet.visibility = View.VISIBLE

                if (sharedViewModel.mode.value.toString() == "transit") {
                    binding.ivDetailView.isVisible = true
                } else {
                    binding.ivDetailView.isVisible = false
                }
                sharedViewModel.refreshIndex()
            }



        }
        binding.ivDetailView.setOnClickListener {
            binding.spinner2tm.visibility = View.VISIBLE
            binding.spinner3rp.visibility = View.VISIBLE
            binding.btnSelectTime.visibility = View.VISIBLE
            sharedViewModel.setIsDepArrNone(0)
            updateTimeButton(0)
            binding.etTime.visibility = View.VISIBLE
            binding.etTime.isVisible = false
            binding.btnSearchDirectionRoutes.visibility = View.VISIBLE

            binding.ivDetailView.isVisible = false
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
                        focusMapOnBounds()
                    }
                }
                handler.postDelayed(checkBoundsRunnable!!, 3000)  // 3초 지연
            }
        }
    }

    private fun updateTimeButton(isDepOrNone: Int) {
        when (isDepOrNone) {
            -1 -> {
                binding.btnSelectTime.text = "출발시각"
                binding.btnSelectTime.setBackgroundResource(R.drawable.btn_radius_malibu)
                binding.etTime.isVisible = true
            }

            1 -> {
                binding.btnSelectTime.text = "도착시각"
                binding.btnSelectTime.setBackgroundResource(R.drawable.btn_radius_arctic)
                binding.etTime.isVisible = true
            }

            else -> {
                binding.btnSelectTime.text = "시간선택"
                binding.btnSelectTime.setBackgroundResource(R.drawable.btn_radius_lilac)
                binding.etTime.isVisible = false
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

    private fun focusMapOnBounds() {
        // 경로를 포함하는 영역 계산하여 지도의 중심을 이동
        val latLngBounds = LatLngBounds.builder()
        sharedViewModel.latLngBounds.value?.forEach {
            latLngBounds.include(LatLng(it.lat, it.lng))
        }
        if (sharedViewModel.latLngBounds.value != null) {
            val bounds = latLngBounds.build()
            val padding = 100
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        } else {
            Log.e("확인 mapf", "LatLngBounds 실패")
        }
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
        Log.d("확인", "onMapReady $myMap")
        googleMap = myMap
        setLine(myMap)

        setupMapListeners()
    }


    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        Log.d("확인 userLoca fr", "$userLatLng")
                        sharedViewModel.setUserLocation(userLatLng)

                        val locationUtils = LocationUtils()
                        val country = locationUtils.getCountryFromLatLng(
                            requireContext(),
                            it.latitude,
                            it.longitude
                        )
                        country?.let {
                            Log.d("확인 나라", "$it")
                            sharedViewModel.setCountry(it)
                        }

                    } ?: run {
                        Toast.makeText(requireContext(), "1 위치 얻기 실패", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "2 위치 얻기 실패", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            Log.d("확인", "1")
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.d("확인", "2")
                // 권한이 부여되었으므로 현재 위치를 받아옴
                getCurrentLocation()
            } else {
                Log.d("확인", "3")
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        Log.d("확인", "4")
    }

    override fun onClickTimeButton(ampm: Int, hour: Int, minute: Int) {
        val setAmpm = if (ampm == 0) "오전" else "오후"
        mAmpm = ampm
        mHour = hour
        mMinute = minute

        val timeText = binding.etTime
        sharedViewModel.setTime(hour, minute)

        if (timeText != null) {
            val setHour = if (hour < 10) "0${hour}" else hour
            val setMinute = if (minute < 2) "0${minute}" else minute
            val result = "${setAmpm}  ${setHour} : ${setMinute}"
            timeText.setText(result)

        }
    }
}