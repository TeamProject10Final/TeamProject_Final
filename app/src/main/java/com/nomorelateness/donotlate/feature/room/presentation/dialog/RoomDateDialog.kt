package com.nomorelateness.donotlate.feature.room.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.donotlate.databinding.FragmentRoomDateDialogBinding


class RoomDateDialog(
    pickInterface: DatePickerInterface,
    year: Int,
    month: Int,
    day: Int
) : DialogFragment() {

    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null
    private var pickInterface: DatePickerInterface

    init {
        this.year = year
        this.month = month
        this.day = day
        this.pickInterface = pickInterface
    }

    private var _binding: FragmentRoomDateDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomDateDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val datePicker = binding.datepicker


        datePicker.setOnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
            binding.tvDateConfirm.setOnClickListener {
                this.pickInterface?.onClickDateButton(year, monthOfYear, dayOfMonth)
                dismiss()
            }
        }
        binding.tvDateCancel.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

interface DatePickerInterface {
    fun onClickDateButton(year: Int, month: Int, day: Int)
}