package com.example.donotlate.feature.friends.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentFriendsRequestBinding
import com.example.donotlate.feature.friends.presentation.adapter.SearchUserAdapter
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModelFactory
import kotlinx.coroutines.launch

class FriendsRequestFragment : Fragment() {
    private val friendsViewModel: FriendsViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        FriendsViewModelFactory(
            appContainer.getFriendsListFromFirebaseUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.searchUserByIdUseCase,
            appContainer.makeAFriendRequestUseCase,
            appContainer.getUserDataUseCase,
            appContainer.getFriendRequestsStatusUseCase,
            appContainer.getFriendRequestListUseCase,
            appContainer.acceptFriendRequestsUseCase
        )
    }

    private lateinit var fromId:String

    private lateinit var searchUserAdapter: SearchUserAdapter

    private var _binding: FragmentFriendsRequestBinding? = null
    private val binding get() = _binding!!

    private val userList: MutableList<FriendsUserModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsRequestBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchUserAdapter = SearchUserAdapter {user ->
            val dialog = FriendsRequestDialogFragment.newInstance(user)
            dialog.show(parentFragmentManager,"RequestDialogFragment")
        }
        binding.rvFriend.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvFriend.adapter = searchUserAdapter

        binding.btnFriendSearch.setOnClickListener {
            val searchId = binding.etFriendSearch.text.toString().trim()
            Log.d("FriendsRequestFragment", "Search ID: $searchId")
            friendsViewModel.searchUserById(searchId)

        }

        observeViewModel()
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            friendsViewModel.searchUserList.collect{result ->
                Log.d("FriendsRequestFragment", "Observed Results: $result")
                searchUserAdapter.submitList(result)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}