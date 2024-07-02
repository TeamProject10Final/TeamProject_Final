package com.nomorelateness.donotlate.feature.room.presentation.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.presentation.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.nomorelateness.donotlate.databinding.FragmentRoomMapBinding

class RoomMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRoomMapBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
        )
    }

    private lateinit var mGoogleMap: GoogleMap

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            val data = it.getParcelable("data", PlaceModel::class.java)
//            Log.d("debug3", "${data}")
//            if (data != null) {
//                roomViewModel.setMapData(data)
//                Log.d("확인 setMapData", "${data}")
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomMapBinding.inflate(inflater, container, false)


        initMap()

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
        }

        return binding.root
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
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
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
            val title = it.address

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
                Toast.makeText(requireContext(), "${resources.getString(R.string.toast_room_text4)}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchQuery() {
        val etQuery = binding.etRoomMapSearch.text.toString()
        if (etQuery.trim().isEmpty()) {
            Toast.makeText(requireContext(), "${resources.getString(R.string.toast_room_text5)}", Toast.LENGTH_SHORT).show()
        } else {
            roomViewModel.updateQuery(etQuery)
            val bottomFragment = RoomMapBottomFragment()
            bottomFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme)
            bottomFragment.show(parentFragmentManager, "tag")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}