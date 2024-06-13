package com.example.donotlate.feature.friends.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.toFormattedString
import com.example.donotlate.databinding.FragmentRequestDialogBinding
import com.example.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel
import com.example.donotlate.feature.friends.presentation.model.FriendsUserModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModel
import com.example.donotlate.feature.friends.presentation.viewmodel.FriendsViewModelFactory
import kotlinx.coroutines.launch

class FriendsAcceptDialogFragment : DialogFragment() {

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
    private var item: FriendRequestWithUserDataModel? = null

    private var _binding: FragmentRequestDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getParcelable(ARG_ITEM)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRequestDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item?.let {
            binding.tvNameTitle.text = it.friendRequestModel.fromUserName
            binding.tvEmail.text = it.userDataModel.email
            binding.tvCreateAt.text = it.userDataModel.createdAt.toFormattedString()

            val toId = it.friendRequestModel.toId
            val fromId = it.friendRequestModel.fromId

            val requestId = if(fromId > toId) "${fromId}_${toId}" else "${toId}_${fromId}"

            Log.d("requestIdTest", "if(fromId > toId) \"${fromId}_${toId}\" else \"${toId}_${fromId}\"")

            binding.btnFriendRequest.setOnClickListener {
                acceptToFriendRequest(requestId)
                Log.d("requestIdTest", "${requestId}")
                dismiss()
            }
        }
    }

    private fun acceptToFriendRequest(requestId:String){
        lifecycleScope.launch {
            friendsViewModel.acceptToFriendRequest(requestId)
            Log.d("requestIdTest", "${requestId}")

        }
    }

    private fun observeViewModel(){
        lifecycleScope.launch {
            friendsViewModel.requestResult
        }
    }

    companion object {
        private const val ARG_ITEM = "item"

        fun newInstance(item: FriendRequestWithUserDataModel): FriendsAcceptDialogFragment {
            val fragment = FriendsAcceptDialogFragment()
            val args = Bundle()
            args.putParcelable(ARG_ITEM, item)
            fragment.arguments = args
            return fragment
        }
    }
}

