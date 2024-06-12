package com.example.donotlate.feature.main.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.example.donotlate.feature.room.presentation.dialog.LogoutFragmentDialog
import com.example.donotlate.feature.room.presentation.view.RoomActivity
import com.example.donotlate.feature.searchPlace.presentation.search.PlaceSearchFragment
import com.example.donotlate.feature.setting.SettingFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getUserDataUseCase,
            appContainer.getAllUsersUseCase,
            appContainer.getCurrentUserUseCase
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

    private fun placeButton() {
        binding.layoutMainPlace.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(PlaceSearchFragment())
        }
    }

    private fun initButton(){
        startRoom()
        placeButton()
        startPlace()
        startSetting()
        startConsumption()
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
}
