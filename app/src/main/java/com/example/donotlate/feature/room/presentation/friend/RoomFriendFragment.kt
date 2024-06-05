package com.example.donotlate.feature.room.presentation.friend

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.donotlate.databinding.FragmentRoomFriendBinding

class RoomFriendFragment : Fragment() {

    private var _binding : FragmentRoomFriendBinding? = null
    private val binding get() = _binding!!

    private val friendAdapter: RoomFriendAdapter by lazy {
        RoomFriendAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRoomFriendBinding.inflate(inflater, container, false)

        setTitle()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initFriendList()


    }


    private fun setTitle() {
        val title = SpannableStringBuilder("친구아이가!")
        title.apply {
            setSpan(RelativeSizeSpan(1.2f), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomFriendTitle.text = title
    }

    private fun initFriendList() {
        val data = listOf<FriendListModel>(
            FriendListModel("길동아","",""),
            FriendListModel("길동이","",""),
            FriendListModel("길동이1","",""),
            FriendListModel("길동아2","",""),
            FriendListModel("홍길동","",""),
            FriendListModel("홀홍홍","",""),
        )

        binding.rvFriend.apply {
            adapter = friendAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
        }
        friendAdapter.submitList(data)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}