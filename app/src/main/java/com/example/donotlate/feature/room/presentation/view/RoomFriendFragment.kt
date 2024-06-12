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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentRoomFriendBinding
import com.example.donotlate.feature.room.presentation.adapter.RoomFriendAdapter
import com.example.donotlate.feature.room.presentation.model.RoomUserModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import kotlinx.coroutines.launch

class RoomFriendFragment : Fragment() {

    private val roomViewModel: RoomViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(appContainer.getAllUsersUseCase)
    }

    private var _binding : FragmentRoomFriendBinding? = null
    private val binding get() = _binding!!

    private val friendAdapter by lazy {
        RoomFriendAdapter()
    }

    private val selectedUsers = mutableListOf<RoomUserModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentRoomFriendBinding.inflate(inflater, container, false)
        roomViewModel.getAllUserData()
        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getAllUserList()

    }


    private fun setTitle() {
        val title = SpannableStringBuilder("친구 아이가!")
        title.apply {
            setSpan(RelativeSizeSpan(1.4f), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomFriendTitle.text = title
    }

    private fun getAllUserList(){
        try {
            lifecycleScope.launch {
                roomViewModel.getAllUserData.collect {result ->
                    Log.d("RoomFriendFragment", "User Data: $result")
                    setUpRecyclerView(result)

                }
            }
        }catch (e:Exception){
            Log.e("RoomFragmentError", "Error: ${e.message}")
        }
    }

    private fun setUpRecyclerView(userList:List<RoomUserModel>){
        try{
            binding.rvFriend.apply {
                adapter = friendAdapter
                layoutManager = GridLayoutManager(requireContext(), 4)
            }
            friendAdapter.submitList(userList)
            friendAdapter.itemClick = object : RoomFriendAdapter.ItemClick{
                override fun onClick(view: View, position: Int) {
                    val selectedUser = friendAdapter.currentList[position]
                    if(selectedUsers.contains(selectedUser)){
                        selectedUsers.remove(selectedUser)
                        Log.d("RRRR", "${selectedUsers}")
                    }else{
                        selectedUsers.add(selectedUser)
                        Log.d("RRRR", "${selectedUsers}")
                    }
                }

            }
        }catch (e:Exception){
            Log.e("RecyclerVuewSetupError", "Error: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}