package com.nomorelateness.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.nomorelateness.donotlate.databinding.FragmentViewPagerBinding
import com.nomorelateness.donotlate.feature.room.presentation.adapter.RoomViewPagerAdapter
import com.nomorelateness.donotlate.feature.room.presentation.dialog.BackFragmentDialog

class ViewPagerFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        initViewPager()

        roomViewModel.modelCurrent.observe(viewLifecycleOwner) {

            binding.viewPager.setCurrentItem(it, true)

        }

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
                                showBackFragmentDialog()
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

    //관찰할때랑 collect할 때에는 항상 view의 lifecycle을 따라야 함

    private fun prevPage() {
        roomViewModel.prevPage()
    }

    private fun showBackFragmentDialog(){
        Handler(Looper.getMainLooper()).post{
            val dialog = BackFragmentDialog()

            if(isAdded && !isStateSaved){
                dialog.show(childFragmentManager, "BackFragmentDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}