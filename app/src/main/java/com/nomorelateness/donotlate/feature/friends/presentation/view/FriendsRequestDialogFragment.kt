package com.nomorelateness.donotlate.feature.friends.presentation.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.presentation.CurrentUser
import com.nomorelateness.donotlate.databinding.FragmentRequestDialogBinding
import com.nomorelateness.donotlate.feature.friends.presentation.model.FriendsUserModel
import kotlinx.coroutines.launch

class FriendsRequestDialogFragment : DialogFragment() {

    private val friendsViewModel: FriendsViewModel by viewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        FriendsViewModelFactory(
            appContainer.getFriendsListFromFirebaseUseCase,
            appContainer.searchUserByIdUseCase,
            appContainer.makeAFriendRequestUseCase,
            appContainer.getFriendRequestsStatusUseCase,
            appContainer.getFriendRequestListUseCase,
            appContainer.acceptFriendRequestsUseCase
        )
    }

    private var _binding: FragmentRequestDialogBinding? = null
    private val binding get() = _binding!!
    private val userData = CurrentUser.userData

    private var fromId = userData?.uId ?: ""
    private var fromUserName = userData?.name ?: ""
    private lateinit var requestID: String
    private var user: FriendsUserModel? = null
    private var toId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        user = arguments?.getParcelable(ARG_USER)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestDialogBinding.inflate(layoutInflater)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        user?.let { user ->
            toId = user.uid

            friendsViewModel.loadFriendRequestList()
            requestID =
                if (fromId > toId) "${fromId}_${toId}" else "${toId}_${fromId} "// 요청 ID 생성

            viewLifecycleOwner.lifecycleScope.launch {
                friendsViewModel.checkFriendRequestStatus(requestID)
            }
            // 친구 요청 상태 확인

            binding.tvNameTitle.text = user.name
            binding.tvEmail.text = user.email

            binding.btnFriendRequest.setOnClickListener {
                sendFriendRequest(toId, fromId, fromUserName, requestID)
                dismiss()
            }
        }

        observeViewModel() // ViewModel 상태 관찰

        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun sendFriendRequest(
        toId: String,
        fromId: String,
        fromUserName: String,
        requestId: String
    ) {
        friendsViewModel.sendFriendRequest(toId, fromId, fromUserName, requestId)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            friendsViewModel.checkRequestStatus.collect { status ->
                status[requestID]?.let {
                    when (it.status) {
                        "request" -> {
                            binding.btnFriendRequest.text =
                                "${resources.getString(R.string.dialog_friend_text1)}"
                            binding.btnFriendRequest.isClickable = false
                            binding.btnFriendRequest.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white_gray
                                )
                            )
                        }

                        "accept" -> {
                            binding.btnFriendRequest.text =
                                "${resources.getString(R.string.dialog_friend_text2)}"
                            binding.btnFriendRequest.isClickable = false
                            binding.btnFriendRequest.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                        }

                        else -> {
                            binding.btnFriendRequest.text =
                                "${resources.getString(R.string.dialog_friend_text3)}"
                            binding.btnFriendRequest.isClickable = true
                            binding.btnFriendRequest.setBackgroundColor(
                                ContextCompat.getColor(requireContext(), R.color.white)
                            )
                        }
                    }
                } ?: run {
                    binding.btnFriendRequest.text =
                        "${resources.getString(R.string.dialog_friend_text3)}"
                    binding.btnFriendRequest.isClickable = true
                    binding.btnFriendRequest.setBackgroundColor(
                        ContextCompat.getColor(requireContext(), R.color.white)
                    )
                }
            }
        }

        lifecycleScope.launch {
            friendsViewModel.requestResult
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val ARG_USER = "user"

        fun newInstance(user: FriendsUserModel): FriendsRequestDialogFragment {
            val fragment = FriendsRequestDialogFragment()
            val args = Bundle()
            args.putParcelable(ARG_USER, user)
            fragment.arguments = args
            return fragment
        }
    }
}
