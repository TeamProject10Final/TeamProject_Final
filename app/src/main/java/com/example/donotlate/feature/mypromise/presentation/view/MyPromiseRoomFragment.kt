package com.example.donotlate.feature.mypromise.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentChatRoomBinding
import com.example.donotlate.feature.mypromise.presentation.viewmodel.ChatRoomViewModel
import com.example.donotlate.feature.mypromise.presentation.viewmodel.ChatRoomViewModelFactory
import com.example.donotlate.feature.main.presentation.model.UserModel
import kotlinx.coroutines.launch

class MyPromiseRoomFragment : Fragment() {

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


    }
}

