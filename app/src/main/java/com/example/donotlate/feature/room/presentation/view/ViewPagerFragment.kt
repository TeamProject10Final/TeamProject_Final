package com.example.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.core.domain.usecase.GetCurrentUserUseCase
import com.example.donotlate.databinding.FragmentViewPagerBinding
import com.example.donotlate.feature.room.presentation.dialog.BackFragmentDialog
import com.example.donotlate.feature.room.presentation.dialog.ResultFragmentDialog
import com.example.donotlate.feature.room.presentation.adapter.RoomViewPagerAdapter
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getAllUsersUseCase,
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.loadToCurrentUserDataUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
            appContainer.getCurrentUserUseCase
        )
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

        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nextPage()
        initViewPager()

    }

    private fun initViewPager() {
        binding.viewPager.isUserInputEnabled = false //뷰 페이저 슬라이드로 페이지 못넘기게 하기
        val viewPager = binding.viewPager
        val dotsIndicator = binding.indicator
        val roomViewPagerAdapter = RoomViewPagerAdapter(this)

        viewPager.adapter = roomViewPagerAdapter
        dotsIndicator.attachTo(viewPager)

        with(binding) {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    when (position) {
                        0 -> {
                            ivRoomBack.setOnClickListener {
                                val dialog = BackFragmentDialog()
                                dialog.show(requireActivity().supportFragmentManager, "BackFragmentDialog")
                            }

                        }
                        1 -> {
                            ivRoomBack.setOnClickListener {
                                prevPage()
                            }
                        }
                        2 -> {
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
        roomViewModel.modelCurrent.observe(requireActivity()) {
            val viewPager = binding.viewPager
            val current = viewPager.currentItem
            viewPager.setCurrentItem(current + 1, true)
        }
    }

    private fun prevPage() {
        val viewPager = binding.viewPager
        val current = viewPager.currentItem
        viewPager.setCurrentItem(current-1, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}