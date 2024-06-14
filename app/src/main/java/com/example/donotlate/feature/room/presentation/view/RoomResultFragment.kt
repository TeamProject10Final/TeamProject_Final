package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentRoomResultBinding
import com.example.donotlate.feature.main.presentation.model.UserModel
import com.example.donotlate.feature.room.presentation.dialog.CancelFragmentDialog
import com.example.donotlate.feature.room.presentation.model.RoomModel
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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


class RoomResultFragment : Fragment(), OnMapReadyCallback {

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getAllUsersUseCase,
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase
        )
    }

    private var _binding: FragmentRoomResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var mGoogleMap: GoogleMap


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
            binding.tvResultDetailPenalty.text = it.penalty
        }

        roomViewModel.selectedUserNames.observe(viewLifecycleOwner) { userNames ->

            displayUserName(userNames)

        }
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fg_Result_Promise_Map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun displayUserName(userNames: List<String>){
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
                    Toast.makeText(requireContext(), "성공?", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(requireContext(), "실패 ㅠㅠ", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun makeARoom(
        roomTitle: String,
        promiseTime: String,
        promiseDate: String,
        destination: String,
        destinationLat: Double,
        destinationLng: Double,
        penalty: String,
        participants: List<String>
    ) {
        lifecycleScope.launch {
            roomViewModel.makeAPromiseRoom(
                roomTitle,
                promiseTime,
                promiseDate,
                destination,
                destinationLat,
                destinationLng,
                penalty,
                participants
            )
        }
        observeViewModel()
    }

    private fun dataToFirebase() {

        val inputData = roomViewModel.inputText.value
        Log.d("data12", "${inputData}")

        val locationData = roomViewModel.locationData.value
        Log.d("data12", "${locationData}")

        val userData = roomViewModel.selectedUserUIds.value
        Log.d("data12", "${userData}")


        makeARoom(
            roomTitle = inputData?.title ?: "",
            promiseDate = inputData?.date ?: "",
            destination = locationData?.name ?: "",
            destinationLat = locationData?.lat ?: 0.0,
            destinationLng = locationData?.lng ?: 0.0,
            penalty = inputData?.penalty ?: "",
            participants = userData ?: emptyList(),
            promiseTime = inputData?.time ?: ""
        )
//                }
//            }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}