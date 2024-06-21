package com.example.donotlate.feature.friends.presentation.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.example.donotlate.databinding.FragmentFriendsRequestBinding
import com.example.donotlate.feature.friends.presentation.adapter.SearchUserAdapter
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel
import kotlinx.coroutines.launch

class FriendsRequestFragment : Fragment() {
    private val friendsViewModel: FriendsViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        FriendsViewModelFactory(
            appContainer.getFriendsListFromFirebaseUseCase,
            appContainer.searchUserByIdUseCase,
            appContainer.makeAFriendRequestUseCase,
            appContainer.getFriendRequestsStatusUseCase,
            appContainer.getFriendRequestListUseCase,
            appContainer.acceptFriendRequestsUseCase
        )
    }

    private lateinit var searchUserAdapter: SearchUserAdapter

    private var _binding: FragmentFriendsRequestBinding? = null
    private val binding get() = _binding!!

    private val userList: MutableList<FriendsUserModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsRequestBinding.inflate(layoutInflater)

        binding.root.setOnClickListener {
            hideKeyboard()
            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        backButton()
        observeViewModel()
        editTextProcess()

        binding.btnFriendSearch.setOnClickListener {
            val searchId = binding.etFriendSearch.text.toString().trim()
            Log.d("FriendsRequestFragment", "Search ID: $searchId")
            friendsViewModel.searchUserById(searchId)

        }
    }

    private fun backButton() {
        binding.ivFriendRequestBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, FriendsFragment())
                .commit()
        }
    }


    private fun editTextProcess() {

        binding.etFriendSearch.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                requireActivity().currentFocus!!.clearFocus()
                handled = true

                val searchId = binding.etFriendSearch.text.toString().trim()
                Log.d("FriendsRequestFragment", "Search ID: $searchId")
                friendsViewModel.searchUserById(searchId)
            }
            handled
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            friendsViewModel.searchUserList.collect { result ->
                Log.d("FriendsRequestFragment", "Observed Results: $result")
                searchUserAdapter.submitList(result)
            }
        }
    }

    private fun setAdapter(){
        searchUserAdapter = SearchUserAdapter { user ->
            val dialog = FriendsRequestDialogFragment.newInstance(user)
            dialog.show(parentFragmentManager, "RequestDialogFragment")
        }
        binding.rvFriend.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvFriend.adapter = searchUserAdapter

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}