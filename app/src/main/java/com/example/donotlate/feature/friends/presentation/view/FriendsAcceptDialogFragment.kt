package com.example.donotlate.feature.friends.presentation.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentRequestDialogBinding
import com.example.donotlate.feature.friends.presentation.model.FriendRequestWithUserDataModel
import kotlinx.coroutines.launch

class FriendsAcceptDialogFragment : DialogFragment() {

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

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        item?.let {
            binding.tvNameTitle.text = it.friendRequestModel.fromUserName
            binding.tvEmail.text = it.userDataModel.email

            val toId = it.friendRequestModel.toId
            val fromId = it.friendRequestModel.fromId
            val requestId = it.friendRequestModel.requestId



            Log.d("requestIdTest", "$requestId")

            binding.btnFriendRequest.setOnClickListener {
                acceptToFriendRequest(requestId)
                Log.d("requestIdTest", "${requestId}")
                dismiss()
            }

            binding.btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun acceptToFriendRequest(requestId:String){
        lifecycleScope.launch {
            friendsViewModel.acceptToFriendRequest(requestId)
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

