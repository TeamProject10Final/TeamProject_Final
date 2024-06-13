package com.example.donotlate.feature.room.presentation.view

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentRoomMapBinding
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoomMapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRoomMapBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getAllUsersUseCase,
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase
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

        setTitle()
        initMap()

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("확인", "onViewStateRestored")

    }

    override fun onResume() {
        super.onResume()

        Log.d("확인", "onResume")

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

        Log.d("확인", "onViewCreated")
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.layout_Room_Map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun sendQuery() {
        binding.btnRoomMapSearch.setOnClickListener {
            val etQuery = binding.etRoomMapSearch.text.toString()
            if (etQuery.trim().isEmpty()) {
                Toast.makeText(requireContext(), "검색어를 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                roomViewModel.updateQuery(etQuery)
                val bottomFragment = RoomMapBottomFragment()
                bottomFragment.show(parentFragmentManager, "tag")
            }
        }
    }

    private fun editTextProcess() {
        binding.etRoomMapSearch.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        val location = LatLng(37.5664056, 126.9778222)
        val cameraPosition = CameraPosition.Builder().target(location).zoom(15f).build()

        mGoogleMap.mapType = GoogleMap.MAP_TYPE_NORMAL // default 노말 생략 가능
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        mGoogleMap.apply {
            val markerOptions = MarkerOptions()
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            markerOptions.position(location)
            markerOptions.title("서울시청")
            addMarker(markerOptions)
        }

        roomViewModel.locationData.observe(viewLifecycleOwner) {

            mGoogleMap = googleMap
            val lat = it.lat
            val lng = it.lng
            val title = it.name
            Log.d("뷰모델", "${roomViewModel.locationData.value}")

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

    private fun setTitle() {
        val title = SpannableStringBuilder("당장 만나,\n목적지를 정해주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.4f), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomMapTitle.text = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}