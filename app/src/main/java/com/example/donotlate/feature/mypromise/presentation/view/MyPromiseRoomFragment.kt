package com.example.donotlate.feature.mypromise.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentMyPromiseRoomBinding
import com.example.donotlate.feature.mypromise.presentation.adapter.PromiseMessageAdapter
import com.example.donotlate.feature.mypromise.presentation.model.MessageModel
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.model.UserModel
import com.example.donotlate.feature.mypromise.presentation.viewmodel.MyPromiseViewModel
import com.example.donotlate.feature.mypromise.presentation.viewmodel.MyPromiseViewModelFactory
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
            appContainer.getMyDataFromFirebaseUseCase

        )
    }

    private lateinit var adapter: PromiseMessageAdapter

    private var _binding: FragmentMyPromiseRoomBinding? = null
    val binding get() = _binding!!

    private var promiseRoom: PromiseModel? = null
    private var currentUserData: UserModel? = null
    private var roomTitle: String? = null
    private var promiseDate: String? = null
    private var roomId: String? = null


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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
//        getCurrentUserData()

        promiseRoom?.let { room ->

            promiseDate = room.promiseDate
            roomTitle = room.roomTitle
            roomId = room.roomId

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
}

