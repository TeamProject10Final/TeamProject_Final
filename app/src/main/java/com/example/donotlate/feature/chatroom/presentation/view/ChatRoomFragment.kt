package com.example.donotlate.feature.chatroom.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentChatRoomBinding
import com.example.donotlate.feature.chatroom.presentation.viewmodel.ChatRoomViewModel
import com.example.donotlate.feature.chatroom.presentation.viewmodel.ChatRoomViewModelFactory
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModelFactory
import com.example.donotlate.feature.main.presentation.model.UserModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch

class ChatRoomFragment : Fragment() {

    private val chatRoomViewModel: ChatRoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        ChatRoomViewModelFactory(
            appContainer.makeAPromiseRoomUseCase
        )
    }

    private var _binding: FragmentChatRoomBinding? = null
    val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val roomTitle = "집에가자"
        val promiseTime = "13:34"
        val promiseDate = "2024.10.22"
        val destination = "서울시 마포구 청계천" //<-- 목적지 주소 <지번 등의 텍스트 주소>
        val destinationLat = 0.0 // <-- 위도
        val destinationLng = 0.0 // <-- 경도
        val penalty = ""
        val participants = listOf<UserModel>()

        binding.btnSend.setOnClickListener {
            makeARoom(roomTitle,
                promiseTime,
                promiseDate,
                destination,
                destinationLat,
                destinationLng,
                penalty,
                participants)
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
        participants: List<UserModel>
    ) {
        lifecycleScope.launch {
            chatRoomViewModel.makeAPromiseRoom(
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
    }
}

