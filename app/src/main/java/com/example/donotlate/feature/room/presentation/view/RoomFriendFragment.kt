package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.example.donotlate.databinding.FragmentRoomFriendBinding

import com.example.donotlate.feature.room.presentation.adapter.RoomFriendAdapter
import com.example.donotlate.feature.room.presentation.dialog.ResultFragmentDialog
import kotlinx.coroutines.launch

class RoomFriendFragment : Fragment() {

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
        )
    }

    private var _binding: FragmentRoomFriendBinding? = null
    private val binding get() = _binding!!

    private lateinit var friendAdapter: RoomFriendAdapter
    private val selectedUserUIds = mutableListOf<String>()
    private val selectedUserNames = mutableListOf<String>()

    private val userData = CurrentUser.userData

    private val mAuth = userData?.uId ?: ""
    private val mName = userData?.name ?: ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomFriendBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
//            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        getFriendsList()
        getAllUserList()
        editTextProcess()
        checkSelectUser()

    }

    private fun getAllUserList() {
        try {
            viewLifecycleOwner.lifecycleScope.launch {
                roomViewModel.friendsList.collect { result ->
                    Log.d("RoomFriendFragment", "User Data: $result")
                    friendAdapter.submitList(result)

                }
            }
        } catch (e: Exception) {
            Log.e("RoomFragmentError", "Error: ${e.message}")
        }
    }

    private fun initRecyclerView() {
        friendAdapter = RoomFriendAdapter(
            onItemClick = { selectedUser ->
                val userUid = selectedUser.uId
                val userName = selectedUser.name

                if (selectedUserUIds.contains(userUid)) {
                    selectedUserUIds.remove(userUid)
                    selectedUserNames.remove(userName)
                } else {
                    selectedUserUIds.add(userUid)
                    selectedUserNames.add(userName)
                }
                if (selectedUserUIds.isNotEmpty()) {
                    selectedUserUIds.remove(mAuth)
                    selectedUserNames.remove(mName)
                }
                saveToSelectedFriendsUIds()
            }
        )

        binding.rvFriend.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvFriend.adapter = friendAdapter
    }

    private fun saveToSelectedFriendsUIds() {
        if (!selectedUserUIds.contains(mAuth) && selectedUserUIds.isNotEmpty()) {
            selectedUserUIds.add(mAuth)
            roomViewModel.setSelectedUserUIds(selectedUserUIds)

            selectedUserNames.add(mName)
            roomViewModel.updateSelectedUserNames(selectedUserNames)
        }
    }

    private fun editTextProcess() {
        binding.etRoomFriendSearch.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    private fun getFriendsList() {
        viewLifecycleOwner.lifecycleScope.launch {
            roomViewModel.getFriendsList()
        }
    }

    private fun checkSelectUser() {
        binding.btnRoomFriendNext.setOnClickListener {
            val userUId = roomViewModel.selectedUserUIds.value
            Log.d("123123", "${userUId}")
            if (userUId != null) {
                val dialog = ResultFragmentDialog()
                dialog.show(requireActivity().supportFragmentManager, "ResultFragmentDialog")
            } else {
                Toast.makeText(requireContext(), "친구를 선택해 주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}