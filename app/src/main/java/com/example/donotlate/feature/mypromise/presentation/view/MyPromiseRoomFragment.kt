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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.donotlate.feature.directionRoute.presentation.LocationUtils
import com.example.donotlate.feature.mypromise.presentation.adapter.PromiseMessageAdapter
import com.example.donotlate.feature.mypromise.presentation.model.MessageModel
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.view.dialog.RadioButtonDialog
import com.example.donotlate.feature.mypromise.presentation.view.dialog.RadioButtonSelectionDialog
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

class MyPromiseRoomFragment : Fragment() {

    private val myPromiseViewModel: MyPromiseRoomViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseRoomViewModelFactory(
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getDirectionsUseCase,
            appContainer.removeParticipantsUseCase,
            appContainer.updateArrivalStatusUseCase
        )
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //아래 코드 지우면 안 됩니다!!!!
    private lateinit var locationCallback: LocationCallback
    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

    companion object {
        private val PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 100
    }

    private lateinit var adapter: PromiseMessageAdapter

    private var _binding: FragmentMyPromiseRoomBinding? = null
    val binding get() = _binding!!

    private var promiseRoom: PromiseModel? = null
    private var currentUserData = CurrentUser.userData
    private var roomId: String? = null
    private var roomDestination: String? = null


    private var hasArrived: Boolean? = null
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                    }
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        checkPermissionAndProceed()
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
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun stopLocationUpdates() {
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
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
        updateArrived()
        observeViewModel2()

        setViewMore()

        promiseRoom?.let { room ->


            roomId = room.roomId

            hasArrived = room.hasArrived[currentUserData?.uId]
            if (hasArrived == true) {
                binding.btnArrived.isClickable = false
                binding.ivRoomMap.visibility = View.GONE
                binding.btnDeparture.visibility = View.GONE
            }

            myPromiseViewModel.startCheckingArrivalStatus(room)
            myPromiseViewModel.setInitialArrivalStatus(room.hasArrived)
            Log.d("확인확인확인", "${room.hasArrived}")


            myPromiseViewModel.setDestinationLatLng(room.destinationLat, room.destinationLng)

            binding.tvRoomTitleDetail.text = room.roomTitle
            binding.tvRoomTitle.text = room.roomTitle
            binding.tvRoomPromiseDate.text = room.promiseDate
            binding.tvRoomPromiseTime.text = room.promiseTime
            if (room.penalty.isNullOrEmpty()) {
                binding.tvRoomPromisePenalty.text = "벌칙은 따로 없습니다!"
            } else {
                binding.tvRoomPromisePenalty.text = room.penalty
            }

            Log.d("확인해보자", "${room.participantsNames.values}")

            val participantNamesList = room.participantsNames.values.toList()
            binding.tvRoomPromiseParticipants.text =
                "참여자: ${participantNamesList.joinToString(", ")}"
            Log.d("확인해보자1", "${participantNamesList}")

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

        binding.btnRoomExit.setOnClickListener {
            val roomId = roomId
            val participantId = currentUserData?.uId
            Log.d("나가기", "정보확인 ${roomId}, ${participantId}")

            if (roomId != null && participantId != null) {
                Log.d("나가기", "실행")
                setExitButton(roomId, participantId)
            }
            observeViewModel1()
        }

        binding.ivRoomMap.setOnClickListener {

//TODO 4 대한민국일 때 이 부분은 건너뜀... showModeDialog 전까지 다 주석처리하고 에러 잡은 뒤 다시 살리기
            checkPermissionAndProceed()

            if (myPromiseViewModel.getCountry() == null) {
                Toast.makeText(context, "다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            } else if (myPromiseViewModel.getCountry() == "대한민국" || myPromiseViewModel.getCountry() == "South Korea") {
                myPromiseViewModel.routeSelectionText.observe(viewLifecycleOwner) {
                    Log.d("확인 routeS 한국", "몇번?")
                    if (it != null) {
                        showDialogSelection(it)
                    } else {
                        Log.d("확인 routeS", "$it")
                    }
                }
            } else {
                showModeDialog()
            }
        }


        binding.btnDeparture.setOnClickListener {
            //TODO 여기서도 checkPermissionAndProceed를 쓰는 게 나은거 맞겠지..?
            checkPermissionAndProceed()
            myPromiseViewModel.setIsDepart(true)
            //여기에 출발 대한 동작 추가하기
            binding.btnDeparture.isVisible = false
            binding.ivRoomMap.isVisible = true
            binding.btnArrived.isVisible = false
        }
    }

    private fun showModeDialog() {
        val selectionDialog = RadioButtonDialog() {

            lifecycleScope.launch {
                checkPermissionAndProceed()
                yield()
//                showDialogSelection()
                myPromiseViewModel.routeSelectionText.observe(viewLifecycleOwner) {
                    Log.d("확인 routeS", "몇번?")
                    if (it != null) {
                        showDialogSelection(it)
                    } else {
                        Log.d("확인 routeS", "$it")
                    }
                }
            }
        }
        selectionDialog.show(childFragmentManager, "RadioButtonDialog")
    }

    private fun showDialogSelection(selections: List<String>) {
        val routeSelectionDialog = RadioButtonSelectionDialog(selections) {
            //라디오 버튼 선택 뒤의 로직
            myPromiseViewModel.afterSelecting()
        }
        routeSelectionDialog.show(childFragmentManager, "RadioButtonSelectionDialog")
    }

    private fun observeViewModel() {
        myPromiseViewModel.distanceBetween.observe(viewLifecycleOwner) {
            //처음에 방 들어가자마자 초기화하기! 거리 계산해두기
            if (it <= 0.2) { //200m
                binding.btnArrived.isVisible = true
                binding.ivRoomMap.isVisible = false
                //도착 버튼이 보이게
                //메시지가 발송되게
            } else {
                if (myPromiseViewModel.getIsDepart()) {
                    //출발했다면
                    binding.tvText.setText("내 위치")
                    binding.btnDeparture.visibility = View.GONE
                    binding.btnArrived.visibility = View.GONE
                    binding.ivRoomMap.visibility = View.VISIBLE
                    //도착 버튼이 보이지 않게
                    //지도 버튼만 보이게
                } else {
                    binding.tvText.setText("출 발")
                    binding.btnDeparture.visibility = View.VISIBLE
                    binding.ivRoomMap.visibility = View.GONE
                    binding.btnArrived.visibility = View.GONE
                }
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

        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseViewModel.hasArrived.collect { arrivalStatus ->

                val uid = currentUserData?.uId
                val isArrived = arrivalStatus[uid] ?: false
                if (isArrived) {
                    Toast.makeText(requireContext(), "약속장소에 도착하였습니다.", Toast.LENGTH_SHORT).show()

                    binding.btnArrived.isClickable = false
                    binding.btnDeparture.visibility = View.GONE
                    binding.ivRoomMap.visibility = View.GONE
                }
            }
        }
    }

    private fun loadToMessageFromFireStore(roomId: String) {
        lifecycleScope.launch {
            myPromiseViewModel.loadMessage(roomId)
        }
    }

    private fun sendMessage(roomId: String, contents: String) {
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
                // old item count 구하기
//                val oldItemCount = adapter.itemCount
                adapter.submitList(message) {
                    binding.rvMessage.scrollToPosition(adapter.itemCount - 1)
                    /*새로운 메시지가 왔을 때 최하단으로 내려가는 로직(이부분 수정해서 올드 아이템갯수랑 아이템갯수랑 리스트의 아이템 갯수 차이를 구해서
                    플로팅 버튼을 띄워서 최하단으로 내려갈 수 있도록 하면 좋을 듯?
                   if (oldItemCount != adapter.itemCount) {
                       binding.rvMessage.scrollToPosition(adapter.itemCount - 1)
                   }*/
                }
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
            //TODO 3 권한 있을 땐 onstart에서 시작되지 않나?
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

                    val locationUtils = LocationUtils()
                    val country = locationUtils.getCountryFromLatLng(
                        requireContext(),
                        it.latitude,
                        it.longitude
                    )
                    country?.let {
                        Log.d("확인 나라", "$it")
                        myPromiseViewModel.setCountry(it)
                    }
                    getDirectionsAndSelection()
                } ?: run {
                    //
                }
            }
            .addOnFailureListener {
                Log.d("확인 getCurrent", "c")
            }
    }

    private fun getDirectionsAndSelection() {
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
                Toast.makeText(context, "위치 권한을 확인해주세요.", Toast.LENGTH_SHORT).show()
                //onLocationPermissionDenied()
            }
        }
        Log.d("확인", "4")
    }

    fun onLocationPermissionDenied() {
        Log.d("확인 denied", "권한 x")
        parentFragmentManager.popBackStack()
    }
    private fun setViewMore() {
        binding.clTopTitleBorder.setOnClickListener {
            if (binding.clTopTitleBorderDetail.visibility == View.VISIBLE) {
                binding.clTopTitleBorderDetail.visibility = View.GONE
                binding.ivPromiseRoom.animate().apply {
                    duration = 100
                    rotation(0f)
                }
            } else {
                binding.clTopTitleBorderDetail.visibility = View.VISIBLE
                binding.ivPromiseRoom.animate().apply {
                    duration = 100
                    rotation(180f)
                }
            }
        }
    }

    private fun updateArrived() {
        binding.btnArrived.setOnClickListener {
            val roomId = roomId!!
            val uid = currentUserData?.uId!!
            viewLifecycleOwner.lifecycleScope.launch {
                myPromiseViewModel.updateArrived(roomId, uid)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentFragmentManager.popBackStack()
        myPromiseViewModel.stopCheckingArrivalStatus()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    //임시
    private fun setExitButton(roomId: String, participantId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseViewModel.exitRoom(roomId, participantId)
        }
    }

    private fun observeViewModel1() {
        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseViewModel.removeParticipantIdResult.collect {
                if (it == true) {
                    Toast.makeText(requireContext(), "나가기 성공", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "나가기 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeViewModel2() {
        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseViewModel.lateUsers.collect { lateUsers ->
                Log.d("Dialog확인", "${lateUsers}")
                if (lateUsers.isNotEmpty()) {
                    if (!this@MyPromiseRoomFragment::dialog.isInitialized || !dialog.isShowing) {
                        showNotArriveDialog(lateUsers)
                        Log.d("Dialog확인", "${lateUsers}")
                    }
                } else {
                    if (this@MyPromiseRoomFragment::dialog.isInitialized && dialog.isShowing) {
                        dialog.dismiss()
                    }
                }
            }
        }
    }

    private fun showNotArriveDialog(userNames: List<String>) {
        dialog = AlertDialog.Builder(requireContext())
            .setTitle("지각자 알림")
            .setMessage("지각자를 공개합니다!\n${userNames.joinToString(", ")}")
            .create()
        dialog.show()
    }
}

