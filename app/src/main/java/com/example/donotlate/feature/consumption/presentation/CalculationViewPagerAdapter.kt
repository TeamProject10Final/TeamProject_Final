package com.example.donotlate.feature.consumption.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class CalculationViewPagerAdapter (fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity){
    val fragments = listOf<Fragment>(
        CalculationFragment1(),
        CalculationFragment2(),
        CalculationFragment3()
    )

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}