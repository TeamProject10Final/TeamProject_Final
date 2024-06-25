package com.example.donotlate.feature.friends.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.databinding.FragmentFriendsBinding
import com.example.donotlate.feature.friends.presentation.adapter.FriendsAdapter
import com.example.donotlate.feature.main.presentation.view.MainFragment
import kotlinx.coroutines.launch

class FriendsFragment : Fragment() {

    private val friendsViewModel: FriendsViewModel by viewModels {
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


    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var friendsAdapter: FriendsAdapter

    private val userData = CurrentUser.userData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(layoutInflater)
        getFriendsList()
        initRecyclerView()
        observeViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRequestList.setOnClickListener {
            setFragment(FriendsRequestListFragment())
        }

        backButton()

        binding.tvTopUserName.text = userData?.name
    }

    private fun backButton() {
        binding.ivFriendBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, MainFragment())
                .commit()
        }
    }

    private fun setFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                /* enter = */ R.anim.slide_in,
                /* exit = */ R.anim.fade_out,
            )
            .replace(R.id.frame, fragment)
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
        viewLifecycleOwner.lifecycleScope.launch {
            friendsViewModel.friendsList.collect { friends ->
                friendsAdapter.submitList(friends)
                Log.d("FriendsFragment", "Observed friends: $friends")
            }
        }
    }

    private fun getFriendsList() {
        viewLifecycleOwner.lifecycleScope.launch {
            friendsViewModel.getFriendsList()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}