package com.nomorelateness.donotlate.feature.mypromise.presentation.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.presentation.CurrentUser
import com.nomorelateness.donotlate.databinding.FragmentMyPromiseRoomBinding
import com.nomorelateness.donotlate.feature.directionRoute.presentation.LocationUtils
import com.nomorelateness.donotlate.feature.mypromise.presentation.adapter.PromiseMessageAdapter
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.dialog.RadioButtonDialog
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.dialog.RadioButtonSelectionDialog
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.dialog.RoomExitDialog
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.dialog.RoomExitInterface
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.dialog.RoomLateDialog
import com.nomorelateness.donotlate.feature.setting.presentation.view.dialog.LoadingDialog
import com.nomorelateness.donotlate.feature.widget.SharedPreferencesHelper
import com.nomorelateness.donotlate.feature.widget.viewmodel.WidgetViewModel
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class MyPromiseRoomFragment : Fragment(R.layout.fragment_my_promise_room), RoomExitInterface {

    private val myPromiseViewModel: MyPromiseRoomViewModel by viewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        MyPromiseRoomViewModelFactory(
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getDirectionsUseCase,
            appContainer.removeParticipantsUseCase,
            appContainer.updateArrivalStatusUseCase,
            appContainer.updateDepartureStatusUseCase
        )
    }
    private val widgetViewModel: WidgetViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    //아래 코드 지우면 안 됩니다!!!!
    private lateinit var locationCallback: LocationCallback

    private val locationUtils by lazy { LocationUtils() }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

    private var promiseMessageAdapter: PromiseMessageAdapter? = null
    private var _binding: FragmentMyPromiseRoomBinding? = null
    val binding get() = _binding!!

    private var currentUserData = CurrentUser.userData
    private val dialog: AlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("지각자 알림")
            .create()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyPromiseRoomBinding.bind(view)
        setOnButtonClickListeners()
        initAdapter()
        setLocationCallbacks()
        checkPermissionAndProceed()
        listenToObservers()
        collectFlows()
        startLocationUpdates()
        setDestinationCountry()

    }

    private fun setDestinationCountry() {
        val destination = myPromiseViewModel.getDestinationLatLng()
        if (destination != null) {
            val destinationCountry = locationUtils.getCountryFromLatLng(
                context = requireContext(),
                lat = destination.latitude,
                lng = destination.longitude
            )
            Log.d("확인 나라 destCountry", "$destinationCountry")
            myPromiseViewModel.setDestCountry(destCountry = destinationCountry)
        }
    }

    private fun setLocationCallbacks() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    location?.let {
                        myPromiseViewModel.setLastLocation(location = it)
                        Log.d("확인 loca cb", "${myPromiseViewModel.originString.value}")
                    }
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun initAdapter() {
        promiseMessageAdapter = PromiseMessageAdapter()
        binding.rvMessage.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessage.adapter = promiseMessageAdapter
    }

    private fun setOnButtonClickListeners() {
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
        binding.btnArrived.setOnClickListener {
            myPromiseViewModel.updateArrived()
            sendMessage("${currentUserData?.name}님께서 목적지에 도착하였습니다.")
        }

        binding.ivChatRoomBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .remove(this)
                .commit()
        }

        binding.btnSend.setOnClickListener {
            val contents = binding.etInputMessage.text.toString()
            if (contents.isNotBlank()) {
                sendMessage(contents = contents)
                binding.etInputMessage.text = null
            }
        }

        binding.btnRoomExit.setOnClickListener {
            showExitDialog()
        }

        binding.ivRoomMap.setOnClickListener {
            //TODO 4 대한민국일 때 이 부분은 건너뜀... showModeDialog 전까지 다 주석처리하고 에러 잡은 뒤 다시 살리기


            checkPermissionAndProceed()




            //TODO 나라 비교하는 코드 추가하기







            myPromiseViewModel.checkCountryAndGetRouteSelection()
        }

        binding.btnDeparture.setOnClickListener {
            //TODO 여기서도 checkPermissionAndProceed를 쓰는 게 나은거 맞겠지..?
            checkPermissionAndProceed()
            myPromiseViewModel.setIsDepart(true)
            sendMessage("${currentUserData?.name}님께서 출발하셨습니다.")
            //여기에 출발 대한 동작 추가하기
            binding.btnDeparture.isVisible = false
            binding.ivRoomMap.isVisible = true
            binding.btnArrived.isVisible = false
            myPromiseViewModel.updateDeparture()
        }
    }

    private fun listenToObservers() {
        myPromiseViewModel.shortExplanations.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                Log.d("확인 shmessage", "nullorempty- $it")
                return@observe
            }
            Log.d("확인", "shortMessage $it")
            sendMessage(contents = it)
        }

        myPromiseViewModel.widgetDeparture.observe(viewLifecycleOwner) {
            updateHasDeparture(requireContext(), it.first, it.second)
            Log.d("확인 출발 버튼", "${it}")
        }

        myPromiseViewModel.widgetArrived.observe(viewLifecycleOwner) {
            updateHasArrived(requireContext(), it.first, it.second)
        }

    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                launch {
                    myPromiseViewModel.distanceStatus.collect {
                        when (it) {
                            DistanceState.In200Meters -> {
                                binding.btnArrived.isVisible = true
                                binding.ivRoomMap.isVisible = false
                                Toast.makeText(
                                    requireContext(),
                                    "목적지로 부터 200m 이내 입니다.",
                                    Toast.LENGTH_LONG
                                ).show()
                                //도착 버튼이 보이게
                                //메시지가 발송되게
                            }

                            DistanceState.Departed -> {
                                //출발했다면
                                binding.btnDeparture.isVisible = false
                                binding.btnArrived.isVisible = false
                                binding.ivRoomMap.isVisible = true
                                //도착 버튼이 보이지 않게
                                //지도 버튼만 보이게
                            }

                            DistanceState.NotDeparted -> {
                                binding.btnDeparture.isVisible = true
                                binding.ivRoomMap.isVisible = false
                                binding.btnArrived.isVisible = false
                            }

                            DistanceState.Nothing -> Unit

                            DistanceState.Arrived -> {
                                binding.btnDeparture.isVisible = false
                                binding.ivRoomMap.isVisible = false
                                binding.btnArrived.isVisible = false
                            }
                        }
                    }
                }
                launch {
                    myPromiseViewModel.myPromiseRoomEvent.collect {
                        when (it) {
                            is MyPromiseRoomEvent.ShowDialogSelection -> showDialogSelection(
                                selections = it.routeSelections
                            )

                            MyPromiseRoomEvent.ShowModeDialog -> showModeDialog()
                        }
                    }
                }
                launch {
                    myPromiseViewModel.isArrived.collect {
                        if (it) {
//                            binding.btnArrived.setBackgroundColor(
//                                resources.getColor(
//                                    R.color.gray,
//                                    requireContext().theme
//                                )
//                            )
                            binding.btnArrived.visibility = View.GONE
                            binding.ivRoomMap.visibility = View.GONE
                            binding.btnDeparture.visibility = View.GONE
                        }
                    }
                }
                launch {
                    myPromiseViewModel.promiseRoom.collect { room ->
                        if (room != null) {
                            binding.tvRoomTitleDetail.text = room.roomTitle
                            binding.tvRoomDestination.text = room.destination
                            binding.tvRoomTitle.text = room.roomTitle
                            binding.tvRoomPromiseDate.text = room.promiseDate
                            binding.tvRoomPromiseTime.text = room.promiseTime
                            if (room.penalty.isEmpty()) {
                                binding.tvRoomPromisePenalty.text = "벌칙은 따로 없습니다!"
                            } else {
                                binding.tvRoomPromisePenalty.text = room.penalty
                            }
                            val participantNames =
                                room.participantsNames.values.toList().joinToString(", ")
                            val roomPromiseParticipants = "참여자: $participantNames"
                            binding.tvRoomPromiseParticipants.text = roomPromiseParticipants
                        }
                    }
                }
                launch {
                    myPromiseViewModel.hasArrived.collect { arrivalStatus ->
                        val uid = currentUserData?.uId
                        val isArrived = arrivalStatus[uid] ?: false
                        if (isArrived) {
                            Toast.makeText(requireContext(), "약속장소에 도착하였습니다.", Toast.LENGTH_SHORT)
                                .show()
                            binding.btnArrived.visibility = View.GONE
                            binding.btnDeparture.visibility = View.GONE
                            binding.ivRoomMap.visibility = View.GONE
                        }
                    }
                }
                launch {
                    myPromiseViewModel.message.collect { message ->
                        Log.d("MyPromiseRoomFragment", "Collected messages: $message")
                        if (message.isEmpty() || promiseMessageAdapter == null) return@collect
                        promiseMessageAdapter!!.submitList(message) {
                            binding.rvMessage.scrollToPosition(promiseMessageAdapter!!.itemCount - 1)
                            /*새로운 메시지가 왔을 때 최하단으로 내려가는 로직(이부분 수정해서 올드 아이템갯수랑 아이템갯수랑 리스트의 아이템 갯수 차이를 구해서
                            플로팅 버튼을 띄워서 최하단으로 내려갈 수 있도록 하면 좋을 듯?
                           if (oldItemCount != adapter.itemCount) {
                               binding.rvMessage.scrollToPosition(adapter.itemCount - 1)
                           }*/
                        }
                    }
                }
                launch {
                    myPromiseViewModel.lateUsers.collect { lateUsers ->
                        Log.d("Dialog확인", "$lateUsers")
                        if (lateUsers.isNotEmpty()) {
                            if (!dialog.isShowing) {
                                showNotArriveDialog(lateUsers)
                                Log.d("Dialog확인", "$lateUsers")
                            }
                        } else {
                            if (dialog.isShowing) {
                                dialog.dismiss()
                            }
                        }
                    }
                }
                launch {
                    myPromiseViewModel.removeParticipantIdResult.collect {
                        if (it == true) {
                            Toast.makeText(requireContext(), "나가기 성공", Toast.LENGTH_SHORT).show()
                        } else if (it == false) {
                            Toast.makeText(requireContext(), "나가기 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                launch {
                    myPromiseViewModel.error.collect {
                        if (it.isNotBlank()) {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun updateHasArrived(context: Context, userId: String, hasArrived: Boolean) {
        SharedPreferencesHelper.updateHasArrived(context, userId, hasArrived)
    }

    private fun updateHasDeparture(context: Context, userId: String, hasDeparture: Boolean) {
        SharedPreferencesHelper.updateHasDeparture(context, userId, hasDeparture)
    }

    private fun showModeDialog() {
        val selectionDialog = RadioButtonDialog {

            myPromiseViewModel.setMode(it)
            myPromiseViewModel.getDirections()
//            myPromiseViewModel.showDialogSelectionAction()
        }
        selectionDialog.show(childFragmentManager, "RadioButtonDialog")
    }

    private fun showDialogSelection(selections: List<String>) {
        Log.d("확인 selection empty", "${selections.isEmpty()}")
        if (selections.isEmpty()) return
        val loading = LoadingDialog()
        loading.show(childFragmentManager, "tag")
        thread(start = true) {
            Thread.sleep(2000)
            activity?.runOnUiThread {
                val routeSelectionDialog = RadioButtonSelectionDialog(selections) {
                    //라디오 버튼 선택 뒤의 로직
                    myPromiseViewModel.setSelectedRouteIndex(it)
                    myPromiseViewModel.afterSelecting()
                }
                routeSelectionDialog.show(childFragmentManager, "RadioButtonSelectionDialog")
                loading.dismiss()
            }
        }
    }

    private fun sendMessage(contents: String) {
        myPromiseViewModel.sendMessage(contents = contents)
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
        if (!hasLocationPermission()) return
        Log.d("확인 getCurrent", "a")
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("확인 getCurrent", "b $location")
                myPromiseViewModel.setLastLocation(location = location)
                if (location != null) {
                    val foundCountry = locationUtils.getCountryFromLatLng(
                        context = requireContext(),
                        lat = location.latitude,
                        lng = location.longitude
                    )
                    Log.d("확인 나라", "$foundCountry")
                    myPromiseViewModel.setFoundCountry(foundCountry = foundCountry)
                }
            }
            .addOnFailureListener {
                Log.d("확인 getCurrent", "c")
            }
    }

    @Deprecated("Deprecated in Java")
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

    override fun onDestroyView() {
        super.onDestroyView()
        myPromiseViewModel.stopCheckingArrivalStatus()
        promiseMessageAdapter = null
        _binding = null
        parentFragmentManager.popBackStack()
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun showNotArriveDialog(userNames: List<String>) {

        val dialog = RoomLateDialog(userNames)
        dialog.show(childFragmentManager, "tag")
    }

    private fun showExitDialog() {
        val dialog = RoomExitDialog(this)
        dialog.show(childFragmentManager, "tag")
    }

    override fun onClickExitRoom() {
        myPromiseViewModel.exitRoom()
        parentFragmentManager.beginTransaction()
            .remove(this)
            .commit()
    }
}

