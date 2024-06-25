package com.example.donotlate.feature.searchPlace.presentation.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val data = it.getParcelable("data", PlaceModel::class.java)
            Log.d("debug1", "$data")
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

        //onBackPressed()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initBackground()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.layout_Place_Detail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnBack.setOnClickListener {
            backButton()
        }

        binding.btnNavigation.setOnClickListener {
            passDestination()
        }

    }

    private fun passDestination() {
        val intent = Intent(requireContext(), DirectionRouteActivity::class.java)
        intent.putExtra("destination", "${searchViewModel.data.value?.name}")
        intent.putExtra("des lat", "${searchViewModel.data.value?.lat}")
        intent.putExtra("des lng", "${searchViewModel.data.value?.lng}")
        Log.d("확인 확인 확인", "${searchViewModel.data.value?.name}")
        startActivity(intent)
    }


    private fun backButton() {
        parentFragmentManager.popBackStack()
//        parentFragmentManager.beginTransaction()
//            .setCustomAnimations(
//                /* enter = */ R.anim.fade_in,
//                /* exit = */ R.anim.slide_out
//            )
//            .remove(this)
//            .commit()
    }

    //외부 뒤로가기 버튼
    private fun onBackPressed() {
        val onBackPressedCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backButton()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            onBackPressedCallback
        )
    }

    //투명 배경 없애기
    private fun initBackground() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val sharedPrefValue = resources.getString(R.string.preference_file_key)
        val darkModeValue =
            sharedPref.getString(getString(R.string.preference_file_key), sharedPrefValue)

        if (darkModeValue == "darkModeOn") {
            binding.constraint.setBackgroundColor(Color.BLACK)
        } else {
            binding.constraint.setBackgroundColor(Color.WHITE)
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
                    } else binding.tvPlaceDetailPhoneNumber.text =
                        "${resources.getString(R.string.search_detail_text4)}"

                    tvPlaceDetailTitle.text = it.name
                    tvPlaceDetailAddress.text = it.address

                    if (it.description != null) {
                        tvPlaceDetailDescription.text =
                            "${it.description[0]}\n${it.description[1]}\n${it.description[2]}\n${it.description[3]}\n${it.description[4]}\n${it.description[5]}\n${it.description[6]}"
                    } else {
                        tvPlaceDetailDescription.text =
                            "${resources.getString(R.string.search_detail_text5)}"
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}