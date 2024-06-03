package com.example.donotlate.feature.room.presentation

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.databinding.FragmentRoomStartBinding
import com.example.donotlate.feature.room.presentation.dialog.BackFragmentDialog

class RoomStartFragment : Fragment() {

    private lateinit var binding: FragmentRoomStartBinding


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


    }


    private fun setTitle() {
        val title = SpannableStringBuilder("우리 지금 만나,\n약속을 잡아주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.5f), 10, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomStartTitle.text = title
    }
}