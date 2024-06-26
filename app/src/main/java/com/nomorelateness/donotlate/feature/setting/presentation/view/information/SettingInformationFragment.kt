package com.nomorelateness.donotlate.feature.setting.presentation.view.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentSettingInformationBinding
import com.nomorelateness.donotlate.feature.setting.presentation.adapter.SettingAdapter
import com.nomorelateness.donotlate.feature.setting.presentation.view.SettingFragment
import com.nomorelateness.donotlate.feature.setting.presentation.view.viewmodel.SettingViewModel


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


        settingViewModel = SettingViewModel()
        val data = settingViewModel.date
        val itemList = arrayListOf("서비스 이용약관", "개인정보 처리방침", "개인정보 수집 ‧ 이용 동의", "위치정보 이용 동의")
        val appList = arrayListOf("오픈소스 라이선스")

        settingAdapter = SettingAdapter(itemList, data)
        with(binding.rvSettingInfo) {
            adapter = settingAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        settingAdapter.itemClick = object : SettingAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> initFragment(SettingServiceFragment())
                    1 -> initFragment(SettingInformationPersonalFragment())
                    2 -> initFragment(SettingPersonalAgreementFragment())
                    3 -> initFragment(SettingLocationAgreementFragment())
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

    private fun backButton() {
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