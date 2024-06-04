package com.example.donotlate.feature.searchPlace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.donotlate.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class SearchPlacesFragment : Fragment(), OnMapReadyCallback {

//    private var _binding: FragmentSeachPlacesBinding? = null
//    private val binding: FragmentSeachPlacesBinding
//        get() = _binding!!

    private val viewModel: SearchPlaceViewModel by viewModels()

    private lateinit var googleMap: GoogleMap
    private lateinit var mapView: MapView
    private lateinit var locationPermission: ActivityResultLauncher<Array<String>>

    //위치 서비스가 gps를 사용해서 위치를 확인
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //위치 값 요청에 대한 갱신 정보를 받는 변수
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search_places,container,false)
        mapView = rootView.findViewById(R.id.fg_map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onMapReady(p0: GoogleMap) {
        val myLocation = LatLng(37.514655, 126.979974)
        p0.moveCamera(CameraUpdateFactory.zoomTo(15f))
        p0.moveCamera(CameraUpdateFactory.newLatLng(myLocation))

        val marker = MarkerOptions().position(myLocation).title("1").snippet("2")
        p0.addMarker(marker)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

//    private fun chipGroupType(type: ChipType){
//        if (binding.etSeachBox.text.isEmpty()){
//            binding.tvDefaultText.isVisible = true
//        }else{
//            binding.tvDefaultText.isVisible = false
//            getLocationPermission()
//        }
//
//        when(type){
//            ChipType.RESTAURANT -> {}
//            ChipType.CAFE -> {}
//            ChipType.MOVIETHEATER -> {}
//            ChipType.PARK -> {}
//            ChipType.SHOPPINGMALL -> {}
//        }
//
//        viewModel.getSearchType.observe(viewLifecycleOwner){
//            //마커
//        }
//    }
//
//
//
//
//
//
//
//
//    private fun getLocationPermission(){//위치 권한 확인
//        locationPermission = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()){ results ->
//            if(results.all{it.value}){//맵 연결
//                (childFragmentManager.findFragmentById(R.id.fg_map) as SupportMapFragment)!!.getMapAsync(this)
//            }else{ //문제가 발생했을 때
//                Toast.makeText(context,"권한 승인이 필요합니다.",Toast.LENGTH_LONG).show()
//            }
//        }
//
//        //권한 요청
//        locationPermission.launch(
//            arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            )
//        )
//    }
//
//    //맵 연결
//    override fun onMapReady(p0: GoogleMap) {
//        googleMap = p0
//
//        val seoul = LatLng(37.566610, 126.978403)
//        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
//        googleMap.apply {
//            val markerOptions = MarkerOptions()
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//            markerOptions.position(seoul)
//            markerOptions.title("서울시청")
//            markerOptions.snippet("Tel:01-120")
//            addMarker(markerOptions)
//        }
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
//        updateLocation()
//    }
//
//    private fun updateLocation(){ //현재 위치 받아오기
//
//        val locationRequest = LocationRequest.create().apply {
//            interval = 1000
//            fastestInterval = 500
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        locationCallback = object : LocationCallback(){
//            //1초에 한번씩 변경된 위치 정보가 onLocationResult 으로 전달
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult?.let{
//                    for (location in it.locations){
//                        Log.d("위치정보",  "위도: ${location.latitude} 경도: ${location.longitude}")
//                        setLastLocation(location) //계속 실시간으로 위치를 받아오고 있기 때문에 맵을 확대해도 다시 줄어듦
//                    }
//                }
//            }
//        }
//        //권한 처리
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback,
//            Looper.myLooper()!!
//        )
//    }
//
//    //위치 탐색
//    private fun setLastLocation(lastLocation: Location){
//        val LATLNG = LatLng(lastLocation.latitude,lastLocation.longitude)
//
//        val makerOptions = MarkerOptions().position(LATLNG).title("현재 위치")
//        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()
//
//        googleMap.addMarker(makerOptions)
//        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}