package com.example.donotlate.feature.setting.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.databinding.FragmentSettingBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.example.donotlate.feature.main.presentation.view.MainPageViewModel
import com.example.donotlate.feature.main.presentation.view.MainPageViewModelFactory
import com.example.donotlate.feature.room.presentation.dialog.LogoutFragmentDialog
import com.example.donotlate.feature.setting.presentation.adapter.SettingAdapter
import com.example.donotlate.feature.setting.presentation.view.information.SettingInformationFragment
import com.example.donotlate.feature.setting.presentation.view.question.SettingQuestionFragment
import kotlinx.coroutines.launch

class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getCurrentUserDataUseCase,
        )
    }

    private val viewModel: SettingsViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        SettingsViewModelFactory(
            sessionManager = appContainer.sessionManager
        )
    }

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingBinding.bind(view)

        listenToFragmentResultListeners()
        darkMode()
        startMyPage()
        setUserData()
        switchMode()
        initView()
        backBt()

        collectFlows()
    }

    private fun listenToFragmentResultListeners() {
        setFragmentResultListener("logoutRequestKey") { _, bundle ->
            val result = bundle.getString("result")
            if (result == "confirm") {
                Log.d("SettingFragment", "Logout result: $result")
                viewModel.logout()
            }
        }
    }

    private fun collectFlows() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
                viewModel.channel.collect {
                    when (it) {
                        is SettingsEvent.Error -> Toast.makeText(
                            requireContext(),
                            it.errorMessage,
                            Toast.LENGTH_LONG
                        ).show()

                        SettingsEvent.LoggedOut -> {
                            CurrentUser.clearData()
                            parentFragmentManager.popBackStack(
                                null,
                                FragmentManager.POP_BACK_STACK_INCLUSIVE
                            )
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.frame, LoginFragment())
                                .commit()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //마이페이지 이동
    private fun startMyPage() {
        binding.constraint.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out
                )
                .replace(R.id.frame, MyPageFragment())
                .addToBackStack("").commit()
        }
    }

    //이름, 이메일 보여주기
    private fun setUserData() {
        val useData = CurrentUser.userData

        if(useData != null){
            binding.tvName.text = useData.name
            binding.tvEmail.text = useData.email
        }
    }

    private fun logoutButton() {
        val dialog = LogoutFragmentDialog()
        dialog.show(requireActivity().supportFragmentManager, "BackFragmentDialog")
        //firebase 로그아웃 기능 추가
    }

    //다크 모드 on/off
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun darkMode() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val sharedPrefValue = resources.getString(R.string.preference_file_key)
        val darkModeValue =
            sharedPref.getString(getString(R.string.preference_file_key), sharedPrefValue)
        val switch = binding.swDarkMode
        switch.setOnClickListener {
            if (darkModeValue == "darkModeOff") {
                //스위치 on
                Log.d("스위치 동작", "스위치 on 다크모드")
                Handler(Looper.getMainLooper()).postDelayed({
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }, 200)

                //앱을 꺼도 다크모드on/off 적용
                with(sharedPref.edit()) {
                    putString(getString(R.string.preference_file_key), "darkModeOn")
                    apply()
                }
                mainPageViewModel.dakeModeChange(false)
            } else {
                //스위치 off
                Log.d("스위치 동작", "스위치 off 라이트모드")
                Handler(Looper.getMainLooper()).postDelayed({
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }, 200)

                with(sharedPref.edit()) {
                    putString(getString(R.string.preference_file_key), "darkModeOff")
                    apply()
                }
                mainPageViewModel.dakeModeChange(true)
            }
        }
    }

    //스위치 상태 지정
    private fun switchMode() {
        lifecycleScope.launch {
            if (mainPageViewModel.dakeMode.value == true) {
                Log.d("스위치", "스위치 위치 off")
                binding.swDarkMode.setChecked(false)
            } else {
                Log.d("스위치", "스위치 위치 on")
                binding.swDarkMode.setChecked(true)
            }
        }
    }

    //어뎁터 연결
    private fun initView() {
//        val settingItemList = arrayListOf("폰트 변경")
        val settingItemList2 = arrayListOf("문의 하기", "앱 정보", "로그 아웃")

//        val adapter1 = SettingAdapter(settingItemList, null)
//        binding.recyclerSetting.adapter = adapter1
//        binding.recyclerSetting.layoutManager = LinearLayoutManager(requireContext())

        val adapter2 = SettingAdapter(settingItemList2, null)
        binding.recyclerSetting2.adapter = adapter2
        binding.recyclerSetting2.layoutManager = LinearLayoutManager(requireContext())


        //앱 설정 아이템 클릭
//        adapter1.itemClick = object : SettingAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                when (position) {
//                    0 -> Toast.makeText(requireActivity(), "기능 준비중입니다", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        //일반 아이템 클릭
        adapter2.itemClick = object : SettingAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> initFragment(SettingQuestionFragment())
                    1 -> initFragment(SettingInformationFragment())
                    2 -> logoutButton()
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

    private fun backBt() {
        //뒤로가기
        binding.ivBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, MainFragment())
                .commit()
        }
    }
}