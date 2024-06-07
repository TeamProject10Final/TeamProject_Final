package com.example.donotlate.feature.searchPlace.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSearchPlacesBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class SearchPlacesFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentSearchPlacesBinding? = null
    private val binding: FragmentSearchPlacesBinding
        get() = _binding!!

    private val viewModel: SearchPlaceViewModel by viewModels()

    private var document : Int = 0

    private lateinit var googleMap: GoogleMap
    private lateinit var locationPermission: ActivityResultLauncher<Array<String>>

    //위치 서비스가 gps를 사용해서 위치를 확인
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //위치 값 요청에 대한 갱신 정보를 받는 변수
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchPlacesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocationPermission()

        binding.ivSearchBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }


    private fun clickChip(){
        binding.cgChipGroup.setOnCheckedStateChangeListener { chipGroup, ints ->
            val selectChip = chipGroup.checkedChipId
            document = selectChip
            when(selectChip){
                R.id.tv_restaurant -> {
                    getChipGroupType(ChipType.RESTAURANT)
                }
                R.id.tv_cafe -> {
                    getChipGroupType(ChipType.CAFE)
                }
                R.id.tv_cinema -> {
                    getChipGroupType(ChipType.MOVIETHEATER)
                }
                R.id.tv_park -> {
                    getChipGroupType(ChipType.PARK)
                }
                R.id.tv_shoppingMall -> {
                    getChipGroupType(ChipType.SHOPPINGMALL)
                }
            }
        }
    }

    private fun getChipGroupType(type: ChipType){

        when(type){
            ChipType.RESTAURANT -> {
                viewModel.getSearchType(location = "", types = "restaurant")
            }
            ChipType.CAFE -> {
                viewModel.getSearchType(location = "", types = "cafe")
            }
            ChipType.MOVIETHEATER -> {
                viewModel.getSearchType(location = "", types = "movieTheater")
            }
            ChipType.PARK -> {
                viewModel.getSearchType(location = "", types = "park")
            }
            ChipType.SHOPPINGMALL -> {
                viewModel.getSearchType(location = "", types = "shoppingMall")
            }
        }

        viewModel.getSearchType.observe(viewLifecycleOwner){
            //맵을 띄우는 뷰와 연결 (데이터와 같이 연결되는 것인지?)
        }
    }

    private fun getLocationPermission(){//위치 권한 확인
        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){ results ->
            if(results.all{it.value}){//맵 연결
                (childFragmentManager.findFragmentById(R.id.fg_map) as SupportMapFragment)!!.getMapAsync(this)
            }else{ //문제가 발생했을 때
                Toast.makeText(context,"권한 승인이 필요합니다.",Toast.LENGTH_LONG).show()
            }
        }

        //권한 요청
        locationPermission.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    //맵 연결
    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        //설정 위치 좌표
        val seoul = LatLng(37.566610, 126.978403)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
        googleMap.apply {
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            markerOptions.position(seoul)
            markerOptions.title("서울시청")
            markerOptions.snippet("Tel:01-120")
            addMarker(markerOptions)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        updateLocation()
    }

    private fun updateLocation(){ //현재 위치 받아오기

        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback(){
            //1초에 한번씩 변경된 위치 정보가 onLocationResult 으로 전달
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let{
                    for (location in it.locations){
                        Log.d("위치정보",  "위도: ${location.latitude} 경도: ${location.longitude}")
                        setLastLocation(location) //계속 실시간으로 위치를 받아오고 있기 때문에 맵을 확대해도 다시 줄어듦
                    }
                }
            }
        }
        //권한 처리
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
            Looper.myLooper()!!
        )
    }

    //위치 탐색
    private fun setLastLocation(lastLocation: Location){
        val LATLNG = LatLng(lastLocation.latitude,lastLocation.longitude)

        val makerOptions = MarkerOptions().position(LATLNG).title("현재 위치")
        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()

        googleMap.addMarker(makerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}