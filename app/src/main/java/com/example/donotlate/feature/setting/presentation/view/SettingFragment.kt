package com.example.donotlate.feature.setting.presentation.view

import android.os.Bundle
import android.util.Log
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
import com.example.donotlate.feature.setting.model.ListType
import com.example.donotlate.feature.setting.presentation.adapter.SettingAdapter
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {
    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getUserDataUseCase,
            appContainer.getAllUsersUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.imageUploadUseCase,
            appContainer.firebaseAuth
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
        Log.d("SettingFragment", "onCreateView called")


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        startMyPage()
        observeViewModel()
        val settingItemList = mutableListOf<ListType>()
        val settingItemList2 = mutableListOf<ListType>()

        settingItemList.apply {
            add(ListType(title = "다크 모드", type = 2))
            add(ListType(title = "폰트 변경", type = 1))

        }

        settingItemList2.apply {
            add(ListType(title = "건의 하기", type = 1))
            add(ListType(title = "앱 정보", type = 1))
            add(ListType(title = "로그 아웃", type = 1))
        }
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
                    0 -> {

                    }
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //마이페이지 이동
    private fun startMyPage() {
        binding.constraint.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, MyPageFragment())
                .addToBackStack("").commit()
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