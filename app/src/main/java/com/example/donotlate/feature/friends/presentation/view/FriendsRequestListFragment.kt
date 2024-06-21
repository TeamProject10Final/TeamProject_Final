package com.example.donotlate.feature.friends.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentFriendsRequestListBinding
import com.example.donotlate.feature.friends.presentation.adapter.FriendRequestsAdapter
import com.example.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel
import kotlinx.coroutines.launch

class FriendsRequestListFragment : Fragment() {

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

    private lateinit var friendRequestsAdapter: FriendRequestsAdapter

    private var _binding: FragmentFriendsRequestListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFriendsRequestListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivFriendRequestListBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame, FriendsFragment()).commit()
        }

        getFriendRequestList()
        setupRecyclerView()
        observeViewModel()
        initBackButton()

    }

    private fun setupRecyclerView(){
        friendRequestsAdapter = FriendRequestsAdapter(emptyList()){ item ->
            onFriendRequestClick(item)
            observeViewModel()
        }
        binding.rvFriendRequestList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriendRequestList.adapter = friendRequestsAdapter
    }

    private fun initBackButton() {
        binding.ivFriendRequestListBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, FriendsFragment())
                .commit()
        }
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            friendsViewModel.friendRequestList.collect{list ->
                friendRequestsAdapter.updateList(list)
            }
        }
    }

    private fun onFriendRequestClick(item: FriendRequestWithUserDataModel){
        val dialog = FriendsAcceptDialogFragment.newInstance(item)
        dialog.show(parentFragmentManager, "RequestDialogFragment")
    }

    private fun getFriendRequestList(){
        viewLifecycleOwner.lifecycleScope.launch {
            friendsViewModel.loadFriendRequestList()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}