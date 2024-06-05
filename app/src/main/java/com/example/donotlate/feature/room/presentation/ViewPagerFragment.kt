package com.example.donotlate.feature.room.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.example.donotlate.databinding.FragmentViewPagerBinding
import com.example.donotlate.feature.room.presentation.dialog.BackFragmentDialog

class ViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

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

        with(binding) {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            btnRoomNext.text = "다음"
                            btnRoomNext.setOnClickListener {
                                nextPage()
                            }
                            ivRoomBack.setOnClickListener {
                                val dialog = BackFragmentDialog()
                                dialog.show(requireActivity().supportFragmentManager, "BackFragmentDialog")
                            }
                        }
                        1 -> {
                            btnRoomNext.text = "다음"
                            btnRoomNext.setOnClickListener {
                                nextPage()
                            }
                            ivRoomBack.setOnClickListener {
                                prevPage()
                            }
                        }
                        2 -> {
                            btnRoomNext.text = "만들기"
                            btnRoomNext.setOnClickListener {  }
                            ivRoomBack.setOnClickListener {
                                prevPage()
                            }
                        }
                    }
                }
            })
        }
    }

    private fun nextPage() {
        val viewPager = binding.viewPager
        val current = viewPager.currentItem
        viewPager.setCurrentItem(current+1, true)
    }

    private fun prevPage() {
        val viewPager = binding.viewPager
        val current = viewPager.currentItem
        viewPager.setCurrentItem(current-1, true)
    }
}