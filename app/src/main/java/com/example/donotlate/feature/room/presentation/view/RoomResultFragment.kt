package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentRoomResultBinding
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomFragment
import com.example.donotlate.feature.room.presentation.dialog.CancelFragmentDialog
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.util.UUID


class RoomResultFragment : Fragment(), OnMapReadyCallback {

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
        )
    }

    private var _binding: FragmentRoomResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var mGoogleMap: GoogleMap

    private lateinit var roomInfo: PromiseModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomResultBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initCancel()
        initView()

        binding.btnRoomResult.setOnClickListener {
            dataToFirebase()

        }


    }

    private fun initView() {
        roomViewModel.inputText.observe(viewLifecycleOwner) {
            binding.tvResultDetailTitle.text = it.title
            binding.tvResultDetailDate.text = it.date
            binding.tvResultDetailTime.text = it.time

            if (it.penalty?.isNotEmpty() == true) {
                binding.tvResultDetailPenalty.text = it.penalty
            } else {
                binding.tvResultDetailPenalty.text = "벌칙은 따로 없어요!"
            }
        }

        roomViewModel.selectedUserNames.observe(viewLifecycleOwner) { userNames ->

            displayUserName(userNames)

        }
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fg_Result_Promise_Map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun displayUserName(userNames: List<String>) {
        val formattedNames = userNames.joinToString(separator = "  ")
        binding.tvResultDetailFriend.text = formattedNames
    }


    private fun initCancel() {
        binding.ivResultBack.setOnClickListener {
            val dialog = CancelFragmentDialog()
            dialog.show(requireActivity().supportFragmentManager, "CancelFragmentDialog")
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            roomViewModel.makeARoomResult.collect{ it ->
                if(it){
                    openPromiseRoomFragment(roomInfo)
                }
            }
        }
    }

    private fun makeARoom(roomInfo: PromiseModel) {
        lifecycleScope.launch {
            roomViewModel.makeAPromiseRoom(roomInfo)
        }

        observeViewModel()
    }

    private fun dataToFirebase() {

        val inputData = roomViewModel.inputText.value

        val locationData = roomViewModel.locationData.value

        val userData = roomViewModel.selectedUserUIds.value

        roomInfo= PromiseModel(
            roomId = UUID.randomUUID().toString(),
            roomTitle = inputData?.title ?: "",
            promiseDate = inputData?.date ?: "",
            destination = locationData?.name ?: "",
            destinationLat = locationData?.lat ?: 0.0,
            destinationLng = locationData?.lng ?: 0.0,
            penalty = inputData?.penalty ?: "",
            participants = userData ?: emptyList(),
            promiseTime = inputData?.time ?: "",
            roomCreatedAt = Timestamp.now()
        )

        makeARoom(roomInfo)
    }


    override fun onMapReady(googleMap: GoogleMap) {
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

    private fun openPromiseRoomFragment(roomInfo: PromiseModel) {
        val fragment = MyPromiseRoomFragment()
        val bundle = Bundle()
        bundle.putParcelable("promiseRoom", roomInfo)
        Log.d("putParcelable", "1: $roomInfo")
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                /* enter = */ R.anim.slide_in,
                /* exit = */ R.anim.fade_out,
            )
            .replace(R.id.frame, fragment)
            .commitAllowingStateLoss()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}