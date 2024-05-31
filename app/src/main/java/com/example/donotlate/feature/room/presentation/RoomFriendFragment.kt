package com.example.donotlate.feature.room.presentation

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.databinding.FragmentRoomFriendBinding

class RoomFriendFragment : Fragment() {

    private lateinit var binding : FragmentRoomFriendBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRoomFriendBinding.inflate(inflater, container, false)

        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movePage()

    }


    private fun setTitle() {
        val title = SpannableStringBuilder("친구아이가!")
        title.apply {
            setSpan(RelativeSizeSpan(1.5f), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomFriendTitle.text = title
    }

    private fun movePage() {
        val viewPager = ViewPagerFragment()

        binding.btnRoomFriend.setOnClickListener {

        }

        //이전 페이지
        binding.ivRoomFriendBack.setOnClickListener {
            viewPager.prevPage()
        }
    }
}