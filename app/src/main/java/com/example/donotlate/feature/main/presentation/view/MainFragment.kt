package com.example.donotlate.feature.main.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMainBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity
import com.example.donotlate.feature.friends.presentation.view.FriendsActivity
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModel
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModelFactory
import com.example.donotlate.feature.minigame.MiniGameFragment
import com.example.donotlate.feature.mypromise.presentation.view.MyPromiseListFragment
import com.example.donotlate.feature.room.presentation.view.RoomActivity
import com.example.donotlate.feature.searchPlace.presentation.search.PlaceSearchFragment
import com.example.donotlate.feature.setting.presentation.view.SettingFragment
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

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

    private lateinit var binding: FragmentMainBinding

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainPageViewModel.getCurrentUserUId()
        setFragmentResultListener("logoutRequestKey") { _, bundle ->
            val result = bundle.getString("result")
            if (result == "confirm") {
                Log.d("SettingFragment", "Logout result: $result")
                mainPageViewModel.logout()
                navigateToMainActivity()
            }
        }

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


    private fun initButton() {
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
            parentFragmentManager.beginTransaction().replace(R.id.frame, SettingFragment())
                .addToBackStack("").commit()
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

    private fun navigateToMainActivity() {
        Log.d("SettingFragment", "Navigating to MainActivity")
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish() // 현재 Activity 종료
    }
}
