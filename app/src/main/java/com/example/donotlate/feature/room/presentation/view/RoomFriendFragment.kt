package com.example.donotlate.feature.room.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.databinding.FragmentRoomFriendBinding
import com.example.donotlate.feature.friends.presentation.view.FriendsRequestFragment

import com.example.donotlate.feature.room.presentation.adapter.RoomFriendAdapter
import com.example.donotlate.feature.room.presentation.dialog.ResultFragmentDialog
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
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
            lifecycleScope.launch {
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
                hideKeyboard()
                requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    private fun getFriendsList() {
        lifecycleScope.launch {
            roomViewModel.getFriendsList()
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
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