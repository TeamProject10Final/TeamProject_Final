package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.donotlate.MyApp
import com.example.donotlate.databinding.FragmentRoomMapBinding
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoomMapFragment : Fragment() {

    private var _binding : FragmentRoomMapBinding? = null
    private val binding get() = _binding!!

//    private val roomViewModel: RoomViewModel by viewModels {
//        val appContainer = (requireActivity().application as MyApp).appContainer
//        RoomViewModelFactory(appContainer.getAllUsersUseCase)
//    }

//수정
    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as MyApp).appContainer
        RoomViewModelFactory(appContainer.getAllUsersUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomMapBinding.inflate(inflater, container, false)

        setTitle()




        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("확인","onViewStateRestored")

    }

    override fun onResume() {
        super.onResume()

        Log.d("확인","onResume")


//        roomViewModel.viewModelScope.launch {
//           roomViewModel.inputText.observe(viewLifecycleOwner){
//                binding.tvTest1.text = it.title
//            }
//        }

//        roomViewModel.inputText.observe(viewLifecycleOwner) { newValue ->
//            binding.apply {
//                tvTest1.text = newValue.title
//                tvTest2.text = newValue.date
//                tvTest3.text = newValue.time
//                tvTest4.text = newValue.penalty.toString()
//            }
//        }
        roomViewModel.inputText.observe(viewLifecycleOwner) { newValue ->
            newValue?.let {
                Log.d("확인", "${roomViewModel.inputText.value} roomviewmodel input")
                binding.apply {
                    Log.d("확인", it.title)
                    tvTest1.text = it.title
                    tvTest2.text = it.date
                    tvTest3.text = it.time
                    tvTest4.text = it.penalty.toString()
                }

            }
        }

        roomViewModel.getAllUserData()
        lifecycleScope.launch {
            roomViewModel.getAllUserData.collect{userList ->
                userList.forEach{ user ->
                    Log.d("User", user.name)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("확인","onViewCreated")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTitle() {
        val title = SpannableStringBuilder("당장 만나,\n목적지를 정해주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.2f), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomMapTitle.text = title
    }

}