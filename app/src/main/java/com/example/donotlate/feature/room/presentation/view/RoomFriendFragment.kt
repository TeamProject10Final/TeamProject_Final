package com.example.donotlate.feature.room.presentation.view

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.activityViewModels
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

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getAllUsersUseCase,
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase
        )
    }

    private var _binding: FragmentRoomFriendBinding? = null
    private val binding get() = _binding!!

    private val friendAdapter by lazy {
        RoomFriendAdapter()
    }

    private val selectedUserUIds = mutableListOf<String>()
    private val selectedUserNames = mutableListOf<String>()

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

    override fun onPause() {
        super.onPause()
//        saveToSelectedFriendsUIds()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        getAllUserList()
        editTextProcess()

    }


    private fun setTitle() {
        val title = SpannableStringBuilder("친구 아이가!")
        title.apply {
            setSpan(RelativeSizeSpan(1.4f), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomFriendTitle.text = title
    }

    private fun getAllUserList() {
        try {
            lifecycleScope.launch {
                roomViewModel.getAllUserData.collect { result ->
                    Log.d("RoomFriendFragment", "User Data: $result")
                    setUpRecyclerView(result)

                }
            }
        } catch (e: Exception) {
            Log.e("RoomFragmentError", "Error: ${e.message}")
        }
    }

    private fun setUpRecyclerView(userList: List<RoomUserModel>) {
        try {
            binding.rvFriend.apply {
                adapter = friendAdapter
                layoutManager = GridLayoutManager(requireContext(), 4)
            }
            friendAdapter.submitList(userList)
            friendAdapter.itemClick = object : RoomFriendAdapter.ItemClick {
                override fun onClick(view: View, position: Int) {
                    val selectedUser = friendAdapter.currentList[position]
                    if (selectedUserUIds.contains(selectedUser.uId)) {
                        selectedUserUIds.remove(selectedUser.uId)
                        Log.d("RRRR", "${selectedUserUIds}")
                    } else {
                        selectedUserUIds.add(selectedUser.uId)
                        Log.d("RRRR", "${selectedUserUIds}")
                    }
                    if (selectedUserNames.contains(selectedUser.name)) {
                        selectedUserNames.remove(selectedUser.name)
                        Log.d("RRRR", "${selectedUserNames}")
                    } else {
                        selectedUserNames.add(selectedUser.name)
                        Log.d("RRRR", "${selectedUserNames}")
                    }
                    saveToSelectedFriendsUIds()
                    updateSelectedUserNames()
                }
            }
        } catch (e: Exception) {
            Log.e("RecyclerVuewSetupError", "Error: ${e.message}")
        }
    }

    private fun saveToSelectedFriendsUIds() {
        roomViewModel.setSelectedUserUIds(selectedUserUIds)
        Log.d("data123", "${selectedUserUIds}")
    }

    private fun updateSelectedUserNames() {
        roomViewModel.updateSelectedUserNames(selectedUserNames)
        Log.d("data12", "${selectedUserNames}")
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

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}