package com.example.donotlate.feature.room.presentation.map

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.databinding.FragmentRoomMapBinding

class RoomMapFragment : Fragment() {

    private lateinit var binding: FragmentRoomMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRoomMapBinding.inflate(inflater, container, false)

        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    private fun setTitle() {
        val title = SpannableStringBuilder("당장 만나,\n목적지를 정해주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.2f), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomMapTitle.text = title
    }
}