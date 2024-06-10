package com.example.donotlate

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.donotlate.databinding.ActivityExampleBinding
//import com.example.donotlate.feature.setting.DirectionResponses
import com.example.donotlate.feature.setting.MapData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
//import com.example.donotlate.feature.setting.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class ExampleActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var locationPermission: ActivityResultLauncher<Array<String>>
    //위치 서비스가 gps를 사용해서 위치를 확인
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    //위치 값 요청에 대한 갱신 정보를 받는 변수
    private lateinit var locationCallback: LocationCallback

    private var originLatitude: Double = 37.555555
    private var originLongitude: Double = 126.970909
    private var destinationLatitude: Double = 37.566610
    private var destinationLongitude: Double = 126.978403

    private val binding by lazy { ActivityExampleBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.example)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //래핑한 API_KEY 가져오기
        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        val apiKey = value.toString()

        //API_KEY의 도움으로 Places API 초기화, 키 검색 및 유효성 검사
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        //지도 초기화 및 맵에 띄우기
        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val gd = findViewById<Button>(R.id.directions)
        gd.setOnClickListener{
            mapFragment.getMapAsync {
                mMap = it
                val originLocation = LatLng(originLatitude, originLongitude)
                mMap.addMarker(MarkerOptions().position(originLocation))
                val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
                mMap.addMarker(MarkerOptions().position(destinationLocation))
                val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
                GetDirection(urll).execute()
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
            }
        }

//        getLocationPermission()


    }




//https://maps.googleapis.com/maps/api/directions/json?
// origin=37.555555%2C126.970909
// &destination=%EA%B2%BD%EB%B3%B5%EA%B6%81
// &key=AIzaSyCAOdeHz6erGcY_sbcEqbEgAETVpirfiV8
// &mode=transit
// &language=ko


    //방향 URL을 생성하는 함수
    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
                "&destination=${dest.latitude},${dest.longitude}" +
                "&sensor=false" +
                "&mode=transit" +
                "&key=$secret" +
                "&language=ko"
    }

    //좌표 URL 문자열을 전달하고 디코딩 폴리라인 함수를 호출하는 내부 클래스
    @SuppressLint("StaticFieldLeak")
    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body!!.string()

            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, MapData::class.java)
                Log.d("로그",respObj.toString())
                val path =  ArrayList<LatLng>()
                for (i in 0 until respObj.routes[0].legs[0].steps.size){
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                Log.d("디버그", e.toString())
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.RED)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    //폴리라인을 디코딩하는 함수
    fun decodePolyline(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }
        return poly
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0!!
        val originLocation = LatLng(originLatitude, originLongitude)
        Log.d("내위치정보","여기는 출발위치")
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(originLocation))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))

//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//        updateLocation()
    }

//    private fun getLocationPermission() {//위치 권한 확인
//        locationPermission = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { results ->
//            if (results.all { it.value }) {//맵 연결
//
//                //래핑한 API_KEY 가져오기
//                val ai: ApplicationInfo = applicationContext.packageManager
//                    .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
//                val value = ai.metaData["com.google.android.geo.API_KEY"]
//                val apiKey = value.toString()
//
//                //API_KEY의 도움으로 Places API 초기화, 키 검색 및 유효성 검사
//                if (!Places.isInitialized()) {
//                    Places.initialize(applicationContext, apiKey)
//                }
//
//                //지도 초기화 및 맵에 띄우기
//                val mapFragment = supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment
//                mapFragment.getMapAsync(this)
//
//                val gd = findViewById<Button>(R.id.directions)
//                gd.setOnClickListener{
//                    mapFragment.getMapAsync {
//                        mMap = it
//                        val originLocation = LatLng(originLatitude, originLongitude)
//                        mMap.addMarker(MarkerOptions().position(originLocation))
//                        Log.d("내위치정보","여긴가?")
//                        val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
//                        mMap.addMarker(MarkerOptions().position(destinationLocation))
//                        Log.d("내위치정보","여기는?")
//                        val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
//                        GetDirection(urll).execute()
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
//                    }
//                }
//
////                (supportFragmentManager.findFragmentById(R.id.fg_map) as SupportMapFragment)!!.getMapAsync(
////                    this
////                )
//            } else { //문제가 발생했을 때
//                Toast.makeText(this, "권한 승인이 필요합니다.", Toast.LENGTH_LONG).show()
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
//
//    private fun updateLocation() { //현재 위치 받아오기
//
//        val locationRequest = LocationRequest.create().apply {
//            interval = 1000
//            fastestInterval = 500
//            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        }
//
//        locationCallback = object : LocationCallback() {
//            //1초에 한번씩 변경된 위치 정보가 onLocationResult 으로 전달
//            override fun onLocationResult(locationResult: LocationResult) {
//                locationResult?.let {
//                    for (location in it.locations) {
//                        originLatitude = location.latitude
//                        originLongitude = location.longitude
//                        Log.d("내위치정보", "위도: ${originLatitude} 경도: ${originLongitude}")
//                        Log.d("내위치정보","뜹니다")
//                        setLastLocation(location) //계속 실시간으로 위치를 받아오고 있기 때문에 맵을 확대해도 다시 줄어듦
//                    }
//                }
//            }
//        }
//        //권한 처리
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest, locationCallback,
//            Looper.myLooper()!!
//        )
//    }
//
//    //위치 탐색
//    private fun setLastLocation(lastLocation: Location) {
//        val LATLNG = LatLng(lastLocation.latitude, lastLocation.longitude)
//
//        val makerOptions = MarkerOptions().position(LATLNG).title("현재 위치")
//        val cameraPosition = CameraPosition.Builder().target(LATLNG).zoom(15.0f).build()
//
//        mMap.addMarker(makerOptions)
//        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//    }


}
