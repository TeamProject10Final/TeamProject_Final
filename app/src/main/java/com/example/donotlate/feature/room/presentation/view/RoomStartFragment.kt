package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.example.donotlate.databinding.FragmentRoomStartBinding
import com.example.donotlate.feature.room.presentation.dialog.DatePickerInterface
import com.example.donotlate.feature.room.presentation.dialog.RoomDateDialog
import com.example.donotlate.feature.room.presentation.dialog.RoomTimeDialog
import com.example.donotlate.feature.room.presentation.dialog.TimePickerInterface
import com.example.donotlate.feature.room.presentation.model.RoomModel
import java.util.Calendar

class RoomStartFragment : Fragment(), TimePickerInterface, DatePickerInterface {

    private lateinit var binding: FragmentRoomStartBinding

    private var mAmpm = 0
    private var mMinute = 0
    private var mHour = 0

    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0

    //수정
    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomStartBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
//            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDate()
        setTime()
        sendToResult()
        editTextProcess()

    }

    private fun editTextProcess() {
        binding.etRoomTitle.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }

        binding.etRoomPenalty.setOnEditorActionListener { v, action, event ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    override fun onPause() {
        super.onPause()
        sendToResult()
    }


    private fun setDate() {

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val getMonth = if (month + 1 < 10) "0${month + 1}" else month + 1
        val getDay = if (day < 10) "0${day}" else day

        binding.tvRoomDate.setText("${year}-${getMonth}-${getDay}")

        binding.tvRoomDate.setOnClickListener {
            val dialog = RoomDateDialog(this, mYear, mMonth, mDay)
            dialog.show(childFragmentManager, "tag")
        }
    }

    private fun setTime() {

        binding.tvRoomTime.setOnClickListener {
            val dialog = RoomTimeDialog(this, mAmpm, mHour, mMinute)
            dialog.show(childFragmentManager, "tag")
        }
    }


    private fun sendToResult() {
        binding.btnRoomStartNext.setOnClickListener {

            val title = binding.etRoomTitle.text.toString()
            val date = binding.tvRoomDate.text.toString()
            val time = binding.tvRoomTime.text.toString()
            val penalty = binding.etRoomPenalty.text.toString()

            val roomList = (RoomModel(
                title,
                date,
                time,
                penalty
            ))
            Log.d("123123", "${roomList}")
            if (title.isNotBlank() && date.isNotBlank() && time.isNotBlank()) {
                roomViewModel.updateText(roomList)
                roomViewModel.setCurrentItem(current = 1)
            } else {
                Toast.makeText(requireContext(), "${resources.getString(R.string.toast_room_text1)}", Toast.LENGTH_SHORT).show()
            }
        }

        Log.d("뷰모델 리스트확인", "${roomViewModel.inputText.value}")
    }

    override fun onClickTimeButton(ampm: Int, hour: Int, minute: Int) {
        val setAmpm = if (ampm == 0) "${resources.getString(R.string.toast_room_text2)}" else "${resources.getString(R.string.toast_room_text3)}"
        mAmpm = ampm
        mHour = hour
        mMinute = minute

        val timeText = binding.tvRoomTime

        if (timeText != null) {
            val setHour = if (hour < 10) "0${hour}" else hour
            val setMinute = if (minute < 2) "0${minute}" else minute
            val result = "${setAmpm}  ${setHour} : ${setMinute}"
            timeText.setText(result)

        }

    }

    override fun onClickDateButton(year: Int, month: Int, day: Int) {

        mYear = year
        mMonth = month
        mDay = day

        val getMonth = if (month + 1 < 10) "0${month + 1}" else month + 1
        val getDay = if (day < 10) "0${day}" else day
        val result = "${year}-${getMonth}-${getDay}"
        binding.tvRoomDate.setText(result)
    }
}