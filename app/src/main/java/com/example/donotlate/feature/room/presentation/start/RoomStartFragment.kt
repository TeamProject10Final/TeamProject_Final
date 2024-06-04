package com.example.donotlate.feature.room.presentation.start

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentRoomStartBinding
import com.example.donotlate.feature.room.RoomViewModel
import java.util.Calendar

class RoomStartFragment : Fragment() {

    private lateinit var binding: FragmentRoomStartBinding

    private val roomViewModel : RoomViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomStartBinding.inflate(inflater, container, false)

        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDate()
        setTime()
        sendData()
    }


    private fun setTitle() {
        val title = SpannableStringBuilder("우리 지금 만나,\n약속을 잡아주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.2f), 10, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomStartTitle.text = title
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val dateText = binding.tvRoomDate
        val data = DatePickerDialog.OnDateSetListener { view, year, month, day ->

            if (dateText != null) {
                val m = if (month + 1 < 10) "0${month + 1}" else month + 1
                val d = if (day < 10) "0${day}" else day
                dateText.text = "${year}-${m}-${d}"

                //result로 보낼 날짜 데이터
                val dateData = dateText.text.toString()
                Log.d("test2", "${dateData}")
                roomViewModel.updateText(dateData)
            }
        }

//        val year = calendar.get(Calendar.YEAR)
//        val month = calendar.get(Calendar.MONTH)
//        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val m = if (month + 1 < 10) "0${month + 1}" else month + 1
//        val d = if (day < 10) "0${day}" else day
//        dateText.text = "${year}-${m}-${d}"

        binding.ivDate.setOnClickListener {

            DatePickerDialog(
                requireContext(),
                R.style.DatePickerStyle,
                data,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setTime() {
        val mCurrentTime = Calendar.getInstance()
        val timeText = binding.tvRoomTime
        val data = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            if (timeText != null) {
                val h = if (hourOfDay < 10) "0${hourOfDay}" else hourOfDay
                val m = if (minute < 10) "0${minute}" else minute
                timeText.text = "${h}시 ${m}분"

                //result로 보낼 시간
                val timeData = timeText.text.toString()
                Log.d("test2", "${timeData}")

            }
        }

//        val hour = mCurrentTime.get(Calendar.HOUR)
//        val min = mCurrentTime.get(Calendar.MINUTE)
//        val h = if (hour < 10) "0${hour}" else hour
//        val m = if (min < 10) "0${min}" else min
//        timeText.text = "${h}시 ${m}분"

        binding.ivTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                R.style.TimePickerTheme,
                data,
                mCurrentTime.get(Calendar.HOUR),
                mCurrentTime.get(Calendar.MINUTE),
                false
            ).show()
        }
    }

    private fun sendData() {

        val titleData = binding.etRoomTitle.text.toString()
        Log.d("test", "${titleData}")
        val penaltyData = binding.etRoomPenalty.text.toString()
        Log.d("test", "${penaltyData}")






    }
}