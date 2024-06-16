package com.example.donotlate.feature.consumption.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.viewpager2.widget.ViewPager2
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentViewPagerBinding

class CalculationActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private val binding by lazy { FragmentViewPagerBinding.inflate(layoutInflater) }
    private val viewModel: SharedViewModel by viewModels {
        val appContainer = (this.application as DoNotLateApplication).appContainer
        SharedViewModelFactory(
            appContainer.insertConsumptionUseCase,
            appContainer.deleteConsumptionUseCase,
            appContainer.getDataCountUseCase
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.viewPagerConstraintLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController


//        //뒤로가기
//        binding.btnBack.setOnClickListener {
//            onBackPressed()
//        }
        initViewPager()
        nextPage()

    }


//    override fun onBackPressed() {
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        // 현재 프래그먼트의 뒤로가기 이벤트를 처리
//        val handled = navController.navigateUp()
//
//        // 현재 프래그먼트가 없거나, 루트 프래그먼트라면 액티비티의 뒤로가기 이벤트 처리...
//        if (!handled) {
//            super.onBackPressed()
//        }
//    }

    private fun initViewPager() {
        binding.viewPager.isUserInputEnabled = false
//        binding.btnRoomNext.visibility = View.GONE
        val viewPager = binding.viewPager
        val dotsIndicator = binding.indicator
        val roomViewPagerAdapter = CalculationViewPagerAdapter(this)

        viewPager.adapter = roomViewPagerAdapter
        dotsIndicator.attachTo(viewPager)

        with(binding) {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    when (position) {
                        0 -> {
                            binding.btnCalShare.visibility = View.GONE
                            ivRoomBack.setOnClickListener {
                                onBackPressed()
                            }
                        }

                        1 -> {
                            binding.btnCalShare.visibility = View.GONE
                            ivRoomBack.setOnClickListener {
                                prevPage()
                            }
                        }

                        2 -> {
                            binding.btnCalShare.visibility = View.VISIBLE
                            share()
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
        viewModel.modelCurrent.observe(this) {
            val viewPager = binding.viewPager
            val current = viewPager.currentItem
            viewPager.setCurrentItem(current + 1, true)
        }
    }

    private fun prevPage() {
        val viewPager = binding.viewPager
        val current = viewPager.currentItem
        viewPager.setCurrentItem(current - 1, true)
    }

    //공유
    private fun share() {
        binding.btnCalShare.setOnClickListener {
            val message = viewModel.buildShareMessage()
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            // 이미지로 저장, intent 넘기기, 다시 여기로 돌아오게 하기 등등...
            // 여기에 추가 코드 작성하기!
        }
    }
}