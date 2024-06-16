package com.example.donotlate.feature.main.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMainBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity
import com.example.donotlate.feature.friends.presentation.view.FriendsActivity
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModel
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModelFactory
import com.example.donotlate.feature.minigame.MiniGameFragment
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseListFragment
import com.example.donotlate.feature.room.presentation.dialog.LogoutFragmentDialog
import com.example.donotlate.feature.room.presentation.view.RoomActivity
import com.example.donotlate.feature.searchPlace.presentation.search.PlaceSearchFragment
import com.example.donotlate.feature.setting.presentation.adapter.SettingAdapter
import com.example.donotlate.feature.setting.presentation.view.MyPageFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getUserDataUseCase,
            appContainer.getAllUsersUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.imageUploadUseCase
        )
    }

    private lateinit var binding: FragmentMainBinding

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("FirebaseMyAuth", "${auth.currentUser?.uid}")
        mainPageViewModel.getCurrentUserUId()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentUserData()
        observeViewModel()
        initButton()

    }

    private fun startRoom() {
        binding.layoutMainRoom.setOnClickListener {
            startActivity(Intent(requireActivity(), RoomActivity::class.java))
        }
    }


    private fun initButton(){
        startRoom()
        startPlace()
        startSetting()
        startConsumption()
        startMiniGame()
        startMyPromise()
        startFriends()
    }

    private fun startPlace() {
        binding.layoutMainPlace.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, PlaceSearchFragment())
                .addToBackStack("").commit()
        }
    }

    private fun startSetting() {
        binding.ivMainSetting.setOnClickListener {
            binding.fragmentMain.openDrawer(GravityCompat.END)
            startMyPage()
            initRecyclerview()
        }
    }


    private fun startConsumption() {
        binding.layoutMainSettle.setOnClickListener {
            val intent = Intent(requireContext(), ConsumptionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startFriends() {
        binding.layoutMainFriend.setOnClickListener {
            val intent = Intent(requireContext(), FriendsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainPageViewModel.getUserData.collect { result ->
                result?.onSuccess { myInfo ->
                    binding.tvMainTitle.text = myInfo.name
                    binding.tvName.text = myInfo.name
                    binding.tvEmail.text = myInfo.email
                    Log.d("observeViewModel", "${myInfo.name}")
                }?.onFailure { e ->
                    throw e
                }
            }
        }
    }

    private fun getCurrentUserData() {
        lifecycleScope.launch {
            mainPageViewModel.getCurrentUser.collect { result ->
                result.onSuccess { mAuth ->
                    if (mAuth.isNullOrBlank()) {
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.frame, LoginFragment()).commit()
                    } else {
                        lifecycleScope.launch { mainPageViewModel.getUserFromFireStore(mAuth) }
                    }
                }.onFailure {
                    parentFragmentManager.beginTransaction().replace(R.id.frame, LoginFragment())
                        .commit()
                }
            }
        }
    }

    private fun startMiniGame() {
        binding.layoutMainGame.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, MiniGameFragment())
                .addToBackStack("").commit()
        }
    }

    private fun startMyPromise() {
        binding.layoutMainReservation.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, MyPromiseListFragment())
                .addToBackStack("").commit()
        }
    }

    private fun initRecyclerview() {
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
    }

    //마이페이지 이동
    private fun startMyPage() {
        binding.constraint.setOnClickListener {
            parentFragmentManager.beginTransaction().add(R.id.main, MyPageFragment())
                .addToBackStack("").commit()
        }
    }

    private fun logoutButton() {
        val dialog = LogoutFragmentDialog()
        dialog.show(requireActivity().supportFragmentManager, "BackFragmentDialog")
        //firebase 로그아웃 기능 추가
    }
}
