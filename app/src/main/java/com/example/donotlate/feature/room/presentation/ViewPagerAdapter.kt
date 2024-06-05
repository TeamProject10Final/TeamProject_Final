package com.example.donotlate.feature.room.presentation

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

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