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
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentFriendsBinding
import com.example.donotlate.feature.friends.presentation.adapter.FriendsAdapter
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModelFactory
import kotlinx.coroutines.launch

class FriendsFragment : Fragment() {

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


    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var friendsAdapter: FriendsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(layoutInflater)
        initRecyclerView()
        getFriendsList()
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRequestList.setOnClickListener {
            setFragment(FriendsRequestListFragment())
        }


        initRecyclerView()
        getFriendsList()
        observeViewModel()
        backButton()

    }

    private fun backButton() {
        binding.ivFriendBack.setOnClickListener {
            val activity = activity as FriendsActivity
            activity.finish()
        }
    }

    private fun setFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().replace(R.id.frame_friends, fragment)
            .addToBackStack("").commit()
    }

    private fun initRecyclerView() {
        friendsAdapter = FriendsAdapter(
            onAddFriendClick = {
                setFragment(FriendsRequestFragment())
        },
            onItemClick = {

            }
        )
        binding.rvFriend.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvFriend.adapter = friendsAdapter
    }


    private fun observeViewModel() {
        lifecycleScope.launch {
            friendsViewModel.friendsList.collect { friends ->
                friendsAdapter.submitList(friends)
                Log.d("FriendsFragment", "Observed friends: $friends")
            }
        }
        lifecycleScope.launch {
            friendsViewModel.currentUserData.collect{userData ->
                userData?.let {
                    binding.tvTopUserName.text = userData.name
                }
            }
        }
    }

    private fun getFriendsList() {
        lifecycleScope.launch {
            friendsViewModel.getFriendsList()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}