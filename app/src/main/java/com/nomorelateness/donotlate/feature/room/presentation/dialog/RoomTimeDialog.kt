package com.nomorelateness.donotlate.feature.room.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.nomorelateness.donotlate.databinding.FragmentRoomTimeDialogBinding

class RoomTimeDialog(
    timePickerInterface: TimePickerInterface,
    ampm: Int,
    hour: Int,
    minute: Int) :
    DialogFragment() {

    private var _binding: FragmentRoomTimeDialogBinding? = null
    private val binding get() = _binding!!

    private val meridiemArr = arrayOf("오전", "오후") // am, pm
    private val hoursArr = Array(12) { (it+1).toString() }
    private val minutesArr = arrayOf("0","5", "10","15", "20","25", "30","35", "40","45", "50","55")

    private var timePickerInterface: TimePickerInterface? = null

    // 선택된 값
    private var ampm: Int? = null
    private var hour: Int? = null
    private var minute: Int? = null

    init {
        this.ampm = ampm
        this.hour = hour
        this.minute = minute
        this.timePickerInterface = timePickerInterface
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRoomTimeDialogBinding.inflate(inflater, container, false)

        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val ampmPick = binding.npAmpm
        val hourPick = binding.npHour
        val minutePick = binding.npMinute


        binding.btnTimeConfirm.setOnClickListener {
            ampm = ampmPick.value
            hour = hourPick.value
            minute = minutePick.value * 5

            this.timePickerInterface?.onClickTimeButton(ampm!!, hour!!, minute!!)
            dismiss()
        }

        binding.btnTimeCancel.setOnClickListener {
            dismiss()
        }

        ampmPick.minValue = 0
        hourPick.minValue = 1
        minutePick.minValue = 0

        //  최대값 설정
        ampmPick.maxValue = meridiemArr.size - 1
        hourPick.maxValue = 12
        minutePick.maxValue = minutesArr.size - 1
        //  array 값 넣기
        ampmPick.displayedValues = meridiemArr
        hourPick.displayedValues = hoursArr
        minutePick.displayedValues = minutesArr

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

interface TimePickerInterface {
    fun onClickTimeButton(ampm: Int, hour: Int, minute: Int)
}