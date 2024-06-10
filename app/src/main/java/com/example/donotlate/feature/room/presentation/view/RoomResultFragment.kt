package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentRoomResultBinding
import com.example.donotlate.feature.room.presentation.dialog.CancelFragmentDialog
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoomResultFragment : Fragment() {

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(appContainer.getAllUsersUseCase)
    }

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentRoomResultBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomResultBinding.inflate(inflater, container, false)


        roomViewModel.inputText.observe(viewLifecycleOwner) { newValue ->
            newValue?.let {
                Log.d("뷰모델 데이터 확인", "${roomViewModel.inputText.value} roomviewmodel input")
                binding.apply {

                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCancel()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun initCancel() {
        binding.ivResultBack.setOnClickListener {
            val dialog = CancelFragmentDialog()
            dialog.show(requireActivity().supportFragmentManager, "CancelFragmentDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}