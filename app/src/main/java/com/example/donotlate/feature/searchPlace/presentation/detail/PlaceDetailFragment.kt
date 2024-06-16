package com.example.donotlate.feature.searchPlace.presentation.detail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentPlaceDetailBinding
import com.example.donotlate.feature.directionRoute.DirectionRouteActivity
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initBackButton()

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.layout_Place_Detail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnNavigation.setOnClickListener{
            val intent = Intent(requireContext(), DirectionRouteActivity::class.java)
            intent.putExtra("destination", "${searchViewModel.data.value?.name}")
            Log.d("확인 확인 확인", "${searchViewModel.data.value?.name}")
            startActivity(intent)
        }

    }

    private fun initBackButton() {
        binding.btnBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this).commit()
        }
    }


    private fun initView() {

        searchViewModel.data.observe(viewLifecycleOwner) {
            if (it != null) {
                with(binding) {
                    ivPlaceDetailTitle.load(it.img) {
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
}