package com.example.donotlate.feature.mypromise.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentMyPromiseRoomBinding
import com.example.donotlate.feature.directionRoute.presentation.DirectionRouteActivity
import com.example.donotlate.feature.mypromise.presentation.adapter.PromiseMessageAdapter
import com.example.donotlate.feature.mypromise.presentation.model.MessageModel
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.model.UserModel
import com.example.donotlate.feature.mypromise.presentation.viewmodel.MyPromiseViewModel
import com.example.donotlate.feature.mypromise.presentation.viewmodel.MyPromiseViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

class MyPromiseRoomFragment : Fragment() {

    private val myPromiseViewModel: MyPromiseViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseViewModelFactory(
            appContainer.loadToMyPromiseListUseCase,
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.getUserDataUseCase,
            appContainer.getMyDataFromFirebaseUseCase,
            appContainer.firebaseAuth,
            appContainer.getDirectionsUseCase

        )
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000


    private lateinit var adapter: PromiseMessageAdapter

    private var _binding: FragmentMyPromiseRoomBinding? = null
    val binding get() = _binding!!

    private var promiseRoom: PromiseModel? = null
    private var currentUserData: UserModel? = null
    private var roomTitle: String? = null
    private var promiseDate: String? = null
    private var roomId: String? = null
    private var roomDestination: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            myPromiseViewModel.currentUserData.collect() { userData ->
                currentUserData = userData
                Log.d("tttt", "${userData}")
            }
        }

        arguments?.let { bundle ->
            promiseRoom = bundle.getParcelable("promiseRoom")
            Log.d("promiseRoom", "Received promiseRoom: $promiseRoom")

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPromiseRoomBinding.inflate(layoutInflater)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//        checkPermission()
//        getCurrentLocation()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        backButton()
//        getCurrentUserData()

        promiseRoom?.let { room ->

            promiseDate = room.promiseDate
            roomTitle = room.roomTitle
            roomId = room.roomId
            roomDestination = room.destination

            binding.tvRoomTitle.text = room.roomTitle
            Log.d("promiseRoom", "promiseRoom Title: ${promiseRoom?.roomTitle}")
            binding.tvRoomPromiseDate.text =
                room.promiseDate ?: throw NullPointerException("Date null")
            Log.d("promiseRoom", "promiseRoom Date: ${promiseRoom?.promiseDate}")
            loadToMessageFromFireStore(room.roomId ?: throw NullPointerException("RoomId Null"))
            Log.d("promiseRoom", "promiseRoom Id: ${promiseRoom?.roomId}")
        }

        binding.btnSend.setOnClickListener {
            val contents = binding.etInputMessage.text.toString()
            val roomId = roomId ?: throw NullPointerException("roomId is Null")
            Log.d("ddddddd1", "$roomId")
            sendMessage(roomId, contents)
            binding.etInputMessage.text = null
        }

        binding.ivRoomMap.setOnClickListener{
            //if
            checkPermissionAndProceed()
        }
    }

    private fun loadToMessageFromFireStore(roomId: String) {
        lifecycleScope.launch {
            myPromiseViewModel.loadMessage(roomId)
        }
    }

    private fun sendMessage(roomId: String, contents: String) {
        Log.d("ddddddd2", "$roomTitle")
        lifecycleScope.launch {
            try {
                val message = MessageModel(
                    senderName = currentUserData?.name
                        ?: throw NullPointerException("User Data Null!"),
                    sendTimestamp = Timestamp.now(),
                    senderId = currentUserData?.uid
                        ?: throw NullPointerException("User Data Null!"),
                    contents = contents,
                    messageId = "",
                    senderProfileUrl = currentUserData?.profileImgUrl ?: throw NullPointerException(
                        "User Data Null!"
                    )
                )
                myPromiseViewModel.sendMessage(roomId, message)
            } catch (e: Exception) {
                Log.e("sendMessage", "Error in sendMessage: $e")
            }


        }
    }

//    fun getCurrentUserData() {
//        lifecycleScope.launch {
//            myPromiseViewModel.currentUserData.mapLatest { userData ->
//                currentUserData = userData
//            }
//        }
//    }

    private fun initAdapter() {

        adapter = PromiseMessageAdapter()
        binding.rvMessage.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessage.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseViewModel.message.collect { message ->
                Log.d("MyPromiseRoomFragment", "Collected messages: $message")
                adapter.submitList(message)
            }
        }
    }

    private fun backButton() {
        binding.ivChatRoomBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun checkPermissionAndProceed() {
        if (hasLocationPermission()) {
            // 권한이 있을 때
            getCurrentLocation()
        } else {
            // 권한이 없을 때
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
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

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        if (hasLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        // userLatLng를 ViewModel에 설정하고 메시지를 보여줌
                        myPromiseViewModel.setUserLocation(userLatLng)
                        shortMessage()
                    } ?: run {
                        Toast.makeText(requireContext(), "1 위치 얻기 실패", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "2 위치 얻기 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
//    private fun getCurrentLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                location?.let {
//                    val userLatLng = LatLng(it.latitude, it.longitude)
//
//                    myPromiseViewModel.setUserLocation(userLatLng)
//                    shortMessage()
//                    //
//                } ?: run {
//                    Toast.makeText(requireContext(), "1 위치 얻기 실패", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//            .addOnFailureListener {
//                Toast.makeText(requireContext(), "2 위치 얻기 실패", Toast.LENGTH_SHORT)
//                    .show()
//            }
//    }

    private fun shortMessage() {
        val currentUserLocation = myPromiseViewModel.getUserLocationString()!!
        roomDestination?.let { it1 ->
            myPromiseViewModel.getDirections(currentUserLocation,
                it1,"transit")
        }
        Log.d("확인 확인 확인", "${currentUserLocation}")
        Log.d("확인 확인 확인", "dest : ${roomDestination}")

        val shortmessage = myPromiseViewModel.shortExplanations.value
        val roomId = roomId ?: throw NullPointerException("roomId is Null")
        Log.d("확인", "shortMessage $shortmessage")
        if (shortmessage != null) {
            sendMessage(roomId, shortmessage)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            Log.d("확인","1")
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.d("확인","2")
                // 권한이 부여되었으므로 현재 위치를 받아옴
                getCurrentLocation()
            } else {
                Log.d("확인","3")
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        Log.d("확인","4")
    }
}

