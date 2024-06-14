package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentRoomResultBinding
import com.example.donotlate.feature.room.presentation.dialog.CancelFragmentDialog
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RoomResultFragment : Fragment() {

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getAllUsersUseCase,
            appContainer.makeAPromiseRoomUseCase
        )
    }

    private var _binding: FragmentRoomResultBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d("RoomResultFragment", "onCreateView called")

        _binding = FragmentRoomResultBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("RoomResultFragment", "onViewCreated called")
        initCancel()
        roomViewModel.selectedUserUIds.observe(viewLifecycleOwner) { uids ->
            Log.d("SelectedUserIds", uids.toString())
            Log.d("asdasdasd", "ㅠㅠ")
        }

        roomViewModel.selectedUserNames.observe(viewLifecycleOwner) {userName ->

        }
        observeViewmodel()


    }

    private fun observeViewModel() {

        lifecycleScope.launch {
            roomViewModel.makeARoomResult.collect { it ->
                if (it) {
                    Toast.makeText(requireContext(), "성공?", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "실패 ㅠㅠ", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
    }

    fun observeViewmodel() {

    }

//    fun observeViewmodel(){
//        lifecycleScope.launch {
//            roomViewModel.selectedUserUIds.collect {
//                val uids = it
//
//                val resultUid = uids
//                Log.d("resultUid", "${resultUid}")
//            }
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}