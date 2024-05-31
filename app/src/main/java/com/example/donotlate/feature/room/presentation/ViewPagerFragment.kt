package com.example.donotlate.feature.room.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.donotlate.databinding.FragmentViewPagerBinding

class ViewPagerFragment : Fragment() {

    private lateinit var binding : FragmentViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewPager()

    }

    private fun initViewPager() {
        val viewPager = binding.viewPager
        val dotsIndicator = binding.indicator
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        dotsIndicator.attachTo(viewPager)
    }

    fun nextPage() {
//        val viewPager = binding.viewPager
//        val current = viewPager.currentItem
//        viewPager.setCurrentItem(current+1, true)
    }

    fun prevPage() {
//        val viewPager = binding.viewPager
//        val current = viewPager.currentItem
//        viewPager.setCurrentItem(current-1, true)
    }
}