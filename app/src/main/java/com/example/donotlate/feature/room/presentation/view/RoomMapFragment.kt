package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.example.donotlate.databinding.FragmentRoomMapBinding
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import com.example.donotlate.feature.searchPlace.presentation.mapper.PlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.launch

class RoomMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRoomMapBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getAllUsersUseCase,
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.loadToCurrentUserDataUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
            appContainer.getCurrentUserUseCase
        )
    }

    private lateinit var mGoogleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val data = it.getParcelable("data", PlaceModel::class.java)
            Log.d("debug3", "${data}")
            if (data != null) {
                roomViewModel.setMapData(data)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomMapBinding.inflate(inflater, container, false)

//        setTitle()
        initMap()

        binding.root.setOnClickListener {
            hideKeyboard()
            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        roomViewModel.getAllUserData()
        lifecycleScope.launch {
            roomViewModel.getAllUserData.collect { userList ->
                userList.forEach { user ->
                    Log.d("User", user.name)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextProcess()
        sendQuery()
        checkLocation()
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.layout_Room_Map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun sendQuery() {
        binding.btnRoomMapSearch.setOnClickListener {

            searchQuery()

        }
    }

    private fun editTextProcess() {
        binding.etRoomMapSearch.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                requireActivity().currentFocus!!.clearFocus()
                handled = true

                searchQuery()

            }
            handled
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        //나중에 현재 위치로 변경
        mGoogleMap = googleMap
        val location = LatLng(37.5664056, 126.9778222)
        val cameraPosition = CameraPosition.Builder().target(location).zoom(15f).build()

        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mGoogleMap.apply {
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            markerOptions.position(location)
            markerOptions.title("서울시청")
            addMarker(markerOptions)
        }

        roomViewModel.locationData.observe(viewLifecycleOwner) {

            val lat = it.lat
            val lng = it.lng
            val title = it.name

            val location = LatLng(lat, lng)
            val cameraPosition = CameraPosition.Builder().target(location).zoom(15f).build()

            mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            mGoogleMap.apply {
                val markerOptions = MarkerOptions()
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                markerOptions.position(location)
                markerOptions.title(title)
                addMarker(markerOptions)
            }
        }
    }


    private fun checkLocation() {
        binding.btnRoomMapNext.setOnClickListener {
            val location = roomViewModel.locationData.value
            if (location != null) {
                roomViewModel.setCurrentItem(current = 2)
            } else {
                Toast.makeText(requireContext(), "목적지를 검색해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchQuery() {
        val etQuery = binding.etRoomMapSearch.text.toString()
        if (etQuery.trim().isEmpty()) {
            Toast.makeText(requireContext(), "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
        } else {
            roomViewModel.updateQuery(etQuery)
            val bottomFragment = RoomMapBottomFragment()
            bottomFragment.show(parentFragmentManager, "tag")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}