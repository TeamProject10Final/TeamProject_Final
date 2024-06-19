package com.example.donotlate.feature.setting.presentation.view.information

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSettingInformationBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.example.donotlate.feature.setting.presentation.adapter.SettingAdapter
import com.example.donotlate.feature.setting.presentation.view.SettingFragment
import com.example.donotlate.feature.setting.presentation.view.viewmodel.SettingViewModel


class SettingInformationFragment : Fragment() {

    private var _binding: FragmentSettingInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingAdapter: SettingAdapter
    private lateinit var settingViewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        backButton()
    }

    private fun initView() {

        binding.clSettingInfoPersonal.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out
                )
                .replace(R.id.frame, SettingInformationPersonalFragment())
                .addToBackStack(null)
                .commit()
        }


        settingViewModel = SettingViewModel()
        val data = settingViewModel.date
        val itemList = arrayListOf("개인정보 수집 ‧ 이용 동의", "위치정보 이용 동의")
        val appList = arrayListOf("오픈소스 라이선스")

        settingAdapter = SettingAdapter(itemList, data)
        with(binding.rvSettingInfo) {
            adapter = settingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        settingAdapter.itemClick = object : SettingAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> initFragment(SettingPersonalAgreementFragment())
                    1 -> initFragment(SettingLocationAgreementFragment())
                }
            }
        }

        settingAdapter = SettingAdapter(appList, null)
        with(binding.rvSettingInfoApp) {
            adapter = settingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        settingAdapter.itemClick = object : SettingAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> initFragment(SettingLicenseFragment())
                }
            }
        }
    }

    private fun initFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                /* enter = */ R.anim.slide_in,
                /* exit = */ R.anim.fade_out
            )
            .replace(R.id.frame, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun backButton(){
        //뒤로가기
        binding.ivSettingInfoBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
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