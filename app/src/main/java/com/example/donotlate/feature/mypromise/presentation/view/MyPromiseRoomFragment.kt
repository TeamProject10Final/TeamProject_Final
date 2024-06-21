package com.example.donotlate.feature.mypromise.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.databinding.FragmentMyPromiseRoomBinding
import com.example.donotlate.feature.mypromise.presentation.adapter.PromiseMessageAdapter
import com.example.donotlate.feature.mypromise.presentation.model.MessageModel
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.view.dialog.RadioButtonDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

class MyPromiseRoomFragment : Fragment() {

    private val myPromiseViewModel: MyPromiseRoomViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseRoomViewModelFactory(
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getDirectionsUseCase,
        )
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //아래 코드 지우면 안 됩니다!!!!
    private lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private lateinit var adapter: PromiseMessageAdapter

    private var _binding: FragmentMyPromiseRoomBinding? = null
    val binding get() = _binding!!

    private var promiseRoom: PromiseModel? = null
    private var currentUserData = CurrentUser.userData
    private var roomTitle: String? = null
    private var promiseDate: String? = null
    private var roomId: String? = null
    private var roomDestination: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissionAndProceed()
        arguments?.let { bundle ->
            promiseRoom = bundle.getParcelable("promiseRoom")

        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    location?.let {
                        val userLatLng = LatLng(it.latitude, it.longitude)
                        myPromiseViewModel.setUserLocation(userLatLng)
                        Log.d("확인 loca cb", "${myPromiseViewModel.originString.value}")
                        shortMessage()
                    }
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (hasLocationPermission()) {
            if (::fusedLocationClient.isInitialized.not()) {
                fusedLocationClient =
                    LocationServices.getFusedLocationProviderClient(requireActivity())
            }
            val locationRequest = LocationRequest.create()
                .apply {
                    interval = 10000 //10초
                    fastestInterval = 5000 //5초
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
            //TODO
            if (::locationCallback.isInitialized.not()) {
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            location?.let {
                                val userLatLng = LatLng(it.latitude, it.longitude)
                                myPromiseViewModel.setUserLocation(userLatLng)
                                Log.d("확인 loca cb", "${myPromiseViewModel.originString.value}")
//                                shortMessage()
                            }
                        }
                    }
                }
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPromiseRoomBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        backButton()

        observeViewModel()

        promiseRoom?.let { room ->
            promiseDate = room.promiseDate
            roomTitle = room.roomTitle
            roomId = room.roomId
            roomDestination = room.destination

            myPromiseViewModel.setDestinationLatLng(room.destinationLat, room.destinationLng)

            binding.tvRoomTitle.text = room.roomTitle
            binding.tvRoomPromiseDate.text = room.promiseDate
            loadToMessageFromFireStore(room.roomId)
        }

        binding.btnSend.setOnClickListener {
            val contents = binding.etInputMessage.text.toString()
            val roomId = roomId ?: throw NullPointerException("roomId is Null")

            if (contents.isNotBlank()) {
                sendMessage(roomId, contents)
                binding.etInputMessage.text = null
            }
        }

        binding.ivRoomMap.setOnClickListener {
            val selectionDialog = RadioButtonDialog()
            selectionDialog.show(childFragmentManager, "String")
            checkPermissionAndProceed()
            myPromiseViewModel.setShortDirectionsResult()
        }

        setViewMore(binding.tvRoomTitle, binding.tvRoomPromiseDate, binding.tvRoomTitle)




    }

    private fun observeViewModel() {
        myPromiseViewModel.distanceBetween.observe(viewLifecycleOwner) {
            //처음에 방 들어가자마자 초기화하기! 거리 계산해두기
            if (it <= 0.2) { //200m
                binding.btnArrived.isVisible = true
                binding.ivRoomMap.isVisible = false
                //도착 버튼이 보이게
            } else {
                binding.btnArrived.isVisible = false
                binding.ivRoomMap.isVisible = true
                //도착 버튼이 보이지 않게
            }
        }

        myPromiseViewModel.shortExplanations.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                Log.d("확인 shmessage", "nullorempty- $it")
                return@observe
            }
            val roomId = roomId ?: throw NullPointerException("roomId is Null")
            Log.d("확인", "shortMessage $it")
            sendMessage(roomId, it)
        }

        myPromiseViewModel.originString.observe(viewLifecycleOwner) {
            myPromiseViewModel.calDistance2()
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
                    senderId = currentUserData?.uId
                        ?: throw NullPointerException("User Data Null!"),
                    contents = contents,
                    messageId = "",
                    senderProfileUrl = currentUserData?.profileImgUrl ?: ""
                )
                myPromiseViewModel.sendMessage(roomId, message)
            } catch (e: Exception) {
                Log.e("sendMessage", "Error in sendMessage: $e")
            }
        }
    }

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
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .remove(this)
                .commit()
        }
    }

    private fun checkPermissionAndProceed() {
        if (hasLocationPermission()) {
            // 권한이 있을 때
            startLocationUpdates()
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
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        Log.d("확인 getCurrent", "a")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("확인 getCurrent", "b $location")
                location?.let {
                    val userLatLng = LatLng(it.latitude, it.longitude)
                    myPromiseViewModel.setUserLocation(userLatLng)
                    shortMessage()
                } ?: run {
                    //
                }
            }
            .addOnFailureListener {
                Log.d("확인 getCurrent", "c")
            }
    }

    private fun shortMessage() {
        val currentUserLocation = myPromiseViewModel.originString.value
        myPromiseViewModel.getDirections()

        Log.d("확인 확인 확인", "${currentUserLocation}")
        Log.d("확인 확인 확인", "dest : ${roomDestination}")

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
                startLocationUpdates()
                getCurrentLocation()
            } else {
                Log.d("확인", "3")
            }
        }
        Log.d("확인", "4")
    }

    private fun setViewMore(contentTextView: TextView,contentTextView2: TextView, viewMoreTextView: TextView) {
        // getEllipsisCount()을 통한 더보기 표시 및 구현
        contentTextView.post {
            val lineCount = contentTextView.layout.lineCount
            if (lineCount > 0) {
                if (contentTextView.layout.getEllipsisCount(lineCount - 1) > 0) {
                    // 더보기 표시
                    viewMoreTextView.visibility = View.VISIBLE

                    // 더보기 클릭 이벤트
                    viewMoreTextView.setOnClickListener {
                        contentTextView.maxLines = Int.MAX_VALUE
                        viewMoreTextView.visibility = View.VISIBLE
                        contentTextView2.visibility = View.VISIBLE

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentFragmentManager.popBackStack()
        _binding = null
        myPromiseViewModel.clearMessage()
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }
}

