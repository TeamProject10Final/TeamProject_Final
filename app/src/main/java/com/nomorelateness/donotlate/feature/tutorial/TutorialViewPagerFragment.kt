package com.nomorelateness.donotlate.feature.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentTutorialViewPagerBinding
import com.nomorelateness.donotlate.databinding.FragmentViewPagerBinding
import com.nomorelateness.donotlate.feature.main.presentation.view.MainFragment
import com.nomorelateness.donotlate.feature.room.presentation.adapter.RoomViewPagerAdapter
import com.nomorelateness.donotlate.feature.setting.presentation.view.SettingFragment

class TutorialViewPagerFragment : Fragment() {

    private var _binding: FragmentTutorialViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mWindow = requireActivity().window
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        mWindow.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.statusbar)
        mWindow.navigationBarColor = ContextCompat.getColor(requireActivity(), R.color.statusbar)

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentTutorialViewPagerBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
        initStart()

    }

    private fun initSystemBar() {

        val mWindow = requireActivity().window
        mWindow.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.white)
        mWindow.navigationBarColor = ContextCompat.getColor(requireActivity(), R.color.white)

    }

    private fun initViewPager() {
        val viewPager = binding.vpTutorial
        val dotsIndicator = binding.indicatorTutorial
        dotsIndicator.dotsClickable = false
        val roomViewPagerAdapter = TutorialImageAdapter()

        viewPager.adapter = roomViewPagerAdapter
        dotsIndicator.attachTo(viewPager)

        with(binding) {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            ivTutorialLeft.visibility = View.INVISIBLE
                            ivTutorialRight.setOnClickListener {
                                nextPage()
                            }
                        }

                        1 -> {
                            ivTutorialLeft.visibility = View.VISIBLE
                            ivTutorialLeft.setOnClickListener {
                                prevPage()
                            }
                            ivTutorialRight.setOnClickListener {
                                nextPage()
                            }
                        }

                        2 -> {
                            ivTutorialLeft.setOnClickListener {
                                prevPage()
                            }
                            ivTutorialRight.setOnClickListener {
                                nextPage()
                            }
                        }

                        3 -> {
                            ivTutorialLeft.setOnClickListener {
                                prevPage()
                            }
                            ivTutorialRight.setOnClickListener {
                                nextPage()
                            }
                        }

                        4 -> {
                            ivTutorialLeft.setOnClickListener {
                                prevPage()
                            }
                            ivTutorialRight.setOnClickListener {
                                nextPage()
                            }
                        }

                        5 -> {
                            ivTutorialLeft.setOnClickListener {
                                prevPage()
                            }
                            ivTutorialRight.setOnClickListener {
                                nextPage()
                            }
                        }

                        6 -> {
                            ivTutorialRight.visibility = View.VISIBLE
                            btnTutorialStart.visibility = View.INVISIBLE
                            btnTutorialSkip.visibility = View.VISIBLE
                            ivTutorialLeft.setOnClickListener {
                                prevPage()
                            }
                            ivTutorialRight.setOnClickListener {
                                nextPage()
                            }
                        }

                        7 -> {
                            ivTutorialLeft.setOnClickListener {
                                prevPage()
                            }
                            btnTutorialStart.visibility = View.VISIBLE
                            btnTutorialSkip.visibility = View.INVISIBLE
                            ivTutorialRight.visibility = View.INVISIBLE
                        }
                    }
                }
            })
        }
    }

    private fun nextPage() {
        val viewPager = binding.vpTutorial
        val currentPage = viewPager.currentItem
        viewPager.setCurrentItem(currentPage + 1, true)
    }

    private fun prevPage() {
        val viewPager = binding.vpTutorial
        val currentPage = viewPager.currentItem
        viewPager.setCurrentItem(currentPage - 1, true)
    }

    private fun initStart() {
        binding.btnTutorialSkip.setOnClickListener {
            initSystemBar()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.frame, MainFragment())
                .commit()
        }

        binding.btnTutorialStart.setOnClickListener {
            initSystemBar()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.frame, MainFragment())
                .commit()
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            initSystemBar()
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                .replace(R.id.frame, SettingFragment())
                .commit()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}