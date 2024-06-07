package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.MyApp
import com.example.donotlate.databinding.FragmentRoomFriendBinding
import com.example.donotlate.feature.room.presentation.model.FriendListModel
import com.example.donotlate.feature.room.presentation.adapter.RoomFriendAdapter
import com.example.donotlate.feature.room.presentation.model.UserModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoomFriendFragment : Fragment() {

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val roomViewModel: RoomViewModel by viewModels {
        val appContainer = (requireActivity().application as MyApp).appContainer
        RoomViewModelFactory(appContainer.getAllUsersUseCase)
    }

    private var _binding : FragmentRoomFriendBinding? = null
    private val binding get() = _binding!!

    private val friendAdapter: RoomFriendAdapter by lazy {
        RoomFriendAdapter()
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


        _binding = FragmentRoomFriendBinding.inflate(inflater, container, false)
        roomViewModel.getAllUserData()
        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        initFriendList()
        getAllUserList()

    }


    private fun setTitle() {
        val title = SpannableStringBuilder("친구아이가!")
        title.apply {
            setSpan(RelativeSizeSpan(1.2f), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomFriendTitle.text = title
    }

//    private fun initFriendList() {
//        val data = listOf<FriendListModel>(
//            FriendListModel("길동아","",""),
//            FriendListModel("길동이","",""),
//            FriendListModel("길동이1","",""),
//            FriendListModel("길동아2","",""),
//            FriendListModel("홍길동","",""),
//            FriendListModel("홀홍홍","",""),
//        )
//
//        binding.rvFriend.apply {
//            adapter = friendAdapter
//            layoutManager = GridLayoutManager(requireContext(), 4)
//        }
//        friendAdapter.submitList(data)
//
//
//    }

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

    private fun setUpRecyclerView(userList:List<UserModel>){
        try{
            binding.rvFriend.apply {
                adapter = friendAdapter
                layoutManager = GridLayoutManager(requireContext(), 4)
            }
            friendAdapter.submitList(userList)
        }catch (e:Exception){
            Log.e("RecyclerVuewSetupError", "Error: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}