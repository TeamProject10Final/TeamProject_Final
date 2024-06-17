package com.example.donotlate.feature.friends.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentFriendsRequestListBinding
import com.example.donotlate.databinding.FragmentRequestDialogBinding
import com.example.donotlate.feature.friends.presentation.adapter.FriendRequestsAdapter
import com.example.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModelFactory
import com.example.donotlate.feature.main.presentation.view.MainFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FriendsRequestListFragment : Fragment() {

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

    private lateinit var friendRequestsAdapter: FriendRequestsAdapter

    private var _binding: FragmentFriendsRequestListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frame_friends, FriendsFragment()).commit()
        }

        setupRecyclerView()
        observeViewModel()


    }

    private fun setupRecyclerView(){
        friendRequestsAdapter = FriendRequestsAdapter(emptyList()){ item ->
            onFriendRequestClick(item)
            observeViewModel()
        }
        binding.rvFriendRequestList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriendRequestList.adapter = friendRequestsAdapter
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


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}