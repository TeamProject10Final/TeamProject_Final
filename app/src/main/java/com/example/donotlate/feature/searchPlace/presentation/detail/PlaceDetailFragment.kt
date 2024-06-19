package com.example.donotlate.feature.searchPlace.presentation.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentPlaceDetailBinding
import com.example.donotlate.feature.directionRoute.presentation.DirectionRouteActivity
import com.example.donotlate.feature.searchPlace.api.NetWorkClient
import com.example.donotlate.feature.searchPlace.presentation.mapper.PlaceModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PlaceDetailFragment : Fragment(), OnMapReadyCallback {


    private var _binding: FragmentPlaceDetailBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: PlaceDetailViewModel by viewModels()

    private lateinit var mGoogleMap: GoogleMap


    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val data = it.getParcelable("data", PlaceModel::class.java)
            Log.d("debug1", "${data}")
            if (data != null) {
                searchViewModel.setSelectedItem(data)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initBackButton()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.layout_Place_Detail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnNavigation.setOnClickListener {
            checkPermission()
        }

    }

    private fun initBackButton() {
        binding.btnBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .remove(this)
                .commit()
        }
    }


    private fun initView() {

        searchViewModel.data.observe(viewLifecycleOwner) {
            if (it != null) {
                with(binding) {
                    ivPlaceDetailTitle.load(
                        "https://places.googleapis.com/v1/${it.img}/media?key=${NetWorkClient.API_KEY}&maxHeightPx=500&maxWidthPx=750"
                    ) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(30f))
                    }
                    if (it.phoneNumber != null) {
                        binding.tvPlaceDetailPhoneNumber.text = it.phoneNumber
                    } else binding.tvPlaceDetailPhoneNumber.text = "번호가 제공되지 않습니다."

                    tvPlaceDetailTitle.text = it.name
                    tvPlaceDetailAddress.text = it.address

                    if (it.description != null) {
                        tvPlaceDetailDescription.text =
                            "${it.description[0]}\n${it.description[1]}\n${it.description[2]}\n${it.description[3]}\n${it.description[4]}\n${it.description[5]}\n${it.description[6]}"
                    } else {
                        tvPlaceDetailDescription.text = "영업시간은 가게 측에 문의해주세요."
                    }
                }
            }
        }
    }


    override fun onMapReady(p0: GoogleMap) {
        searchViewModel.data.observe(viewLifecycleOwner) {
            mGoogleMap = p0

            val lat = it.lat
            val lng = it.lng
            val title = it.name
            val number = it.phoneNumber


            //설정 위치 좌표
            val location = LatLng(lat, lng)
            val cameraPosition = CameraPosition.Builder().target(location).zoom(15f).build()

            mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mGoogleMap.apply {
                val markerOptions = MarkerOptions()
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                markerOptions.position(location)
                markerOptions.title(title)
                if (number != null) {
                    markerOptions.snippet("Tel:${number}")
                } else {
                    markerOptions.snippet("전화번호가 제공되지 않습니다.")
                }
                addMarker(markerOptions)
            }
        }


//    private fun initCall() {
//        searchViewModel.data.observe(viewLifecycleOwner) {
//            val number = it.phoneNumber
//            binding.tvPlaceDetailPhoneNumber.setOnClickListener {
//                val call = Uri.parse("tel:${number}")
//                requireActivity().startActivity(Intent(Intent.ACTION_CALL, call))
//            }
//        }
//    }
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
    }

    private fun getCurrentLocation() {
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

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)

                    searchViewModel.setUserLocation(userLatLng)
                    val currentUserLocation = searchViewModel.getUserLocationString()
                    val intent = Intent(requireContext(), DirectionRouteActivity::class.java)
                    intent.putExtra("destination", "${searchViewModel.data.value?.name}")
                    intent.putExtra("userLocation", "${currentUserLocation}")
                    Log.d("확인 확인 확인", "${searchViewModel.data.value?.name}")
                    startActivity(intent)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
}