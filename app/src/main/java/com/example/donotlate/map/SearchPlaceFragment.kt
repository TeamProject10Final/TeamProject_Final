package com.example.donotlate.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.donotlate.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.net.IsOpenResponse.newInstance


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [SeacrhPlaceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchPlaceFragment : Fragment(), GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback {
    // TODO: Rename and change types of parameters


    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mapView: MapView
    private var permissionDenied = false

    //위치 서비스가 gps를 사용해서 위치를 확인
    lateinit var fusedLocationClient: FusedLocationProviderClient

    //위치 값 요청에 대한 갱신 정보를 받는 변수
    lateinit var locationCallback: LocationCallback

    lateinit var locationPermission: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        locationPermission = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            if (results.all { it.value }) {
                val rootView = inflater.inflate(R.layout.fragment_search_place, container, false)
                mapView = rootView.findViewById(R.id.map) as MapView
                mapView.onCreate(savedInstanceState)
                mapView.getMapAsync(this)
            } else { //문제가 발생했을 때
                Toast.makeText(requireActivity(), "권한 승인이 필요합니다.", Toast.LENGTH_LONG).show()
            }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_place, container, false)
    }



    override fun onMapReady(p0: GoogleMap) {

        enableMyLocation()


        val seoul = LatLng(37.566610, 126.978403)
        mGoogleMap = p0
        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
        mGoogleMap.apply {
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

    fun updateLocation() {

        val locationRequest = LocationRequest.create().apply {
            interval = 1000
            fastestInterval = 500
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            //1초에 한번씩 변경된 위치 정보가 onLocationResult 으로 전달된다.
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.let {
                    for (location in it.locations) {
                        Log.d("위치정보", "위도: ${location.latitude} 경도: ${location.longitude}")
                        setLastLocation(location) //계속 실시간으로 위치를 받아오고 있기 때문에 맵을 확대해도 다시 줄어든다.
                    }
                }
            }
        }
        //권한 처리
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()!!
        )
    }



    fun setLastLocation(lastLocation: Location) {
        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)

        val makerOptions = MarkerOptions().position(LATLNG).title("나 여기 있어용~")
        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()

        mGoogleMap.addMarker(makerOptions)
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {

        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {

            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT)
            .show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(requireActivity(), "Current location:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
            return
        }


    }

    override fun onResume() {
        super.onResume()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private fun showMissingPermissionError() {
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}
