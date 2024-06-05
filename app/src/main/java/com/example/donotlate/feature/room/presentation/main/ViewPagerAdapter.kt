package com.example.donotlate.feature.room.presentation.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.donotlate.feature.room.presentation.friend.RoomFriendFragment
import com.example.donotlate.feature.room.presentation.map.RoomMapFragment
import com.example.donotlate.feature.room.presentation.start.RoomStartFragment

class ViewPagerAdapter (fragment: Fragment): FragmentStateAdapter(fragment){
    val fragments = listOf<Fragment>(
        RoomStartFragment(),
        RoomMapFragment(),
        RoomFriendFragment()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}