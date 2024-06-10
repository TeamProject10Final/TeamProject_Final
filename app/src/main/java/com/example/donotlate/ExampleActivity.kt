package com.example.donotlate

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.donotlate.databinding.ActivityExampleBinding
//import com.example.donotlate.feature.setting.DirectionResponses
//import com.example.donotlate.feature.setting.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
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

class ExampleActivity : AppCompatActivity() {

    private lateinit var map: GoogleMap
//    private lateinit var fkip: LatLng
//    private lateinit var monas: LatLng


//    private lateinit var mMap: GoogleMap
//    //출발 좌표
//    private var originLatitude: Double = 28.5021359
//    private var originLongitude: Double = 77.4054901
//    //도착 좌표
//    private var destinationLatitude: Double = 28.5151087
//    private var destinationLongitude: Double = 77.3932163

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
    }
}








//
//        //래핑한 API_KEY 가져오기
//        val ai: ApplicationInfo = applicationContext.packageManager
//            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
//        val value = ai.metaData["com.google.android.geo.API_KEY"]
//        val apiKey = value.toString()
//
//        //API_KEY의 도움으로 Places API 초기화, 키 검색 및 유효성 검사
//        if (!Places.isInitialized()) {
//            Places.initialize(applicationContext, apiKey)
//        }
//
//        //지도 초기화 및 맵에 띄우기
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//
//        val gd = findViewById<Button>(R.id.directions)
//        gd.setOnClickListener{
//            mapFragment.getMapAsync {
//                mMap = it
//                val originLocation = LatLng(originLatitude, originLongitude)
//                mMap.addMarker(MarkerOptions().position(originLocation))
//                val destinationLocation = LatLng(destinationLatitude, destinationLongitude)
//                mMap.addMarker(MarkerOptions().position(destinationLocation))
//                val urll = getDirectionURL(originLocation, destinationLocation, apiKey)
//                GetDirection(urll).execute()
//                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 14F))
//            }
//        }
//    }
//
//
//    //방향 URL을 생성하는 함수
//    private fun getDirectionURL(origin:LatLng, dest:LatLng, secret: String) : String{
//        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}" +
//                "&destination=${dest.latitude},${dest.longitude}" +
//                "&sensor=false" +
//                "&mode=WALK" +
//                "&key=$secret"
//    }
//
//    //좌표 URL 문자열을 전달하고 디코딩 폴리라인 함수를 호출하는 내부 클래스
//    @SuppressLint("StaticFieldLeak")
//    private inner class GetDirection(val url : String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
//        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            val data = response.body!!.string()
//
//            val result =  ArrayList<List<LatLng>>()
//            try{
//                val respObj = Gson().fromJson(data, MapData::class.java)
//                val path =  ArrayList<LatLng>()
//                for (i in 0 until respObj.routes[0].legs[0].steps.size){
//                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
//                }
//                result.add(path)
//            }catch (e:Exception){
//                e.printStackTrace()
//            }
//            return result
//        }
//
//        override fun onPostExecute(result: List<List<LatLng>>) {
//            val lineoption = PolylineOptions()
//            for (i in result.indices){
//                lineoption.addAll(result[i])
//                lineoption.width(10f)
//                lineoption.color(Color.GREEN)
//                lineoption.geodesic(true)
//            }
//            mMap.addPolyline(lineoption)
//        }
//    }
//
//    //폴리라인을 디코딩하는 함수
//    fun decodePolyline(encoded: String): List<LatLng> {
//        val poly = ArrayList<LatLng>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//        while (index < len) {
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lat += dlat
//            shift = 0
//            result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
//            lng += dlng
//            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
//            poly.add(latLng)
//        }
//        return poly
//    }
//
//    override fun onMapReady(p0: GoogleMap) {
//        mMap = p0!!
//        val originLocation = LatLng(originLatitude, originLongitude)
//        mMap.clear()
//        mMap.addMarker(MarkerOptions().position(originLocation))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(originLocation, 18F))
//    }
//}

//        fkip = LatLng(-6.3037978, 106.8693713)
//        monas = LatLng(-6.1890511, 106.8251573)
//
//
//        val mapFragment = supportFragmentManager.findFragmentById(R.id.maps_view) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap
//
//        val markerFkip = MarkerOptions()
//            .position(fkip)
//            .title("FKIP")
//        val markerMonas = MarkerOptions()
//            .position(monas)
//            .title("Monas")
//
//        map.addMarker(markerFkip)
//        map.addMarker(markerMonas)
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(monas, 11.6f))
//
//        val fromFKIP = fkip.latitude.toString() + "," + fkip.longitude.toString()
//        val toMonas = monas.latitude.toString() + "," + monas.longitude.toString()
//
//        val apiServices = RetrofitClient.apiServices(this)
//        apiServices.getDirection(fromFKIP, toMonas, getString(R.string.api_key))
//            .enqueue(object : Callback<DirectionResponses> {
//                override fun onResponse(call: Call<DirectionResponses>, response: Response<DirectionResponses>) {
//                    drawPolyline(response)
//                    Log.d("bisa dong oke", response.message())
//                }
//
//                override fun onFailure(call: Call<DirectionResponses>, t: Throwable) {
//                    Log.e("anjir error", t.localizedMessage)
//                }
//            })
//    }
//    private fun drawPolyline(response: Response<DirectionResponses>) {
//        val shape = response.body()?.routes?.get(0)?.overviewPolyline?.points
//        val polyline = PolylineOptions()
//            .addAll(PolyUtil.decode(shape))
//            .width(8f)
//            .color(Color.RED)
//        map.addPolyline(polyline)
//    }
//
//    private interface ApiServices {
//        @GET("maps/api/directions/json")
//        fun getDirection(@Query("origin") origin: String,
//                         @Query("destination") destination: String,
//                         @Query("key") apiKey: String): Call<DirectionResponses>
//    }
//
//    private object RetrofitClient {
//        fun apiServices(context: Context): ApiServices {
//            val retrofit = Retrofit.Builder()
//                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(context.resources.getString(R.string.base_url))
//                .build()
//
//            return retrofit.create<ApiServices>(ApiServices::class.java)
//        }
//    }
//
//}