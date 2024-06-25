package com.example.donotlate.feature.mypromise.presentation.view.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.BackDialogBinding
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseListFragment
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomFragment
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomViewModel
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseRoomViewModelFactory
import com.example.donotlate.feature.setting.presentation.view.dialog.LoadingDialog
import kotlinx.coroutines.launch

class ExitDialog(
    private val roomId: String,
    private val participantId: String
) : DialogFragment() {

    private val myPromiseViewModel: MyPromiseRoomViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseRoomViewModelFactory(
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getDirectionsUseCase,
            appContainer.removeParticipantsUseCase,
            appContainer.updateArrivalStatusUseCase,
            appContainer.updateDepartureStatusUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true
    }

    private lateinit var binding: BackDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BackDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvDl1.text = "나가기"
        binding.tvDl2.text = "정말 채팅방에서 나가시겠습니까??"

        binding.tvDlCancel.setOnClickListener {
            dismiss()
        }
        binding.tvDlConfirm.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
//                myPromiseViewModel.exitRoom(roomId = roomId, participantId =  participantId)
            }
            dismiss()
            //수정 필요
        }
    }
}