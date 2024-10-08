package com.nomorelateness.donotlate.feature.room.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nomorelateness.donotlate.feature.room.presentation.view.RoomFriendFragment
import com.nomorelateness.donotlate.feature.room.presentation.view.RoomMapFragment
import com.nomorelateness.donotlate.feature.room.presentation.view.RoomStartFragment

class RoomViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
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