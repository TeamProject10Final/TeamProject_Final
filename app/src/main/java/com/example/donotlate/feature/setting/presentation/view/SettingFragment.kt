package com.example.donotlate.feature.setting.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSettingBinding
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModel
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModelFactory
import com.example.donotlate.feature.room.presentation.dialog.LogoutFragmentDialog
import com.example.donotlate.feature.setting.presentation.adapter.SettingAdapter
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {
    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getUserDataUseCase,
            appContainer.getAllUsersUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.imageUploadUseCase
        )
    }

//    private val binding: FragmentSettingBinding by lazy {
//        FragmentSettingBinding.inflate(layoutInflater)
//    }

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        startMyPage()
        observeViewModel()
        val settingItemList = arrayListOf("테마 변경", "폰트 변경")
        val settingItemList2 = arrayListOf("건의하기", "앱 정보", "로그아웃")

        val adapter1 = SettingAdapter(settingItemList)
        binding.recyclerSetting.adapter = adapter1
        binding.recyclerSetting.layoutManager = LinearLayoutManager(requireContext())

        val adapter2 = SettingAdapter(settingItemList2)
        binding.recyclerSetting2.adapter = adapter2
        binding.recyclerSetting2.layoutManager = LinearLayoutManager(requireContext())


        //앱 설정 아이템 클릭
        adapter1.itemClick = object : SettingAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> Toast.makeText(requireActivity(), "기능 준비중입니다", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireActivity(), "기능 준비중입니다", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //일반 아이템 클릭
        adapter2.itemClick = object : SettingAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                when (position) {
                    0 -> Toast.makeText(requireActivity(), "기능 준비중입니다", Toast.LENGTH_SHORT).show()
                    1 -> Toast.makeText(requireActivity(), "기능 준비중입니다", Toast.LENGTH_SHORT).show()
                    2 -> logoutButton()
                }
            }
        }

        //뒤로가기
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        return binding.root
    }

        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //마이페이지 이동
    private fun startMyPage(){
        binding.constraint.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, MypageFragment()).addToBackStack("").commit()
        }
    }

    //이름, 이메일 보여주기
    private fun observeViewModel() {
        lifecycleScope.launch {
            mainPageViewModel.getUserData.collect { result ->
                result?.onSuccess { myInfo ->
                    binding.tvName.text = myInfo.name
                    binding.tvEmail.text = myInfo.email
                }?.onFailure { e ->
                    throw e
                }
            }
        }
    }

    private fun logoutButton() {
            val dialog = LogoutFragmentDialog()
            dialog.show(requireActivity().supportFragmentManager, "BackFragmentDialog")
            //firebase 로그아웃 기능 추가
    }
}