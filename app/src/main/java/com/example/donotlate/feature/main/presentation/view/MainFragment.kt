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
import com.example.donotlate.MainActivity
import com.example.donotlate.MyApp
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMainBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.main.presentation.MainPageViewModel
import com.example.donotlate.feature.main.presentation.MainPageViewModelFactory
import com.example.donotlate.feature.room.presentation.dialog.LogoutFragmentDialog
import com.example.donotlate.feature.room.presentation.view.RoomActivity
import com.example.donotlate.feature.searchPlace.presentation.SearchPlacesFragment
import com.example.donotlate.feature.setting.SettingFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as MyApp).appContainer
        MainPageViewModelFactory(
            appContainer.getUserUseCase,
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
        startRoom()
        placeButton()
        startPlace()
        observeViewModel()
        startSetting()

    }

    private fun startRoom() {
        binding.layoutMainRoom.setOnClickListener {
            startActivity(Intent(requireActivity(), RoomActivity::class.java))
        }
    }

    private fun placeButton() {
        binding.layoutMainPlace.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(SearchPlacesFragment())
        }
    }

    private fun startPlace() {
        binding.layoutMainPlace.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, SearchPlacesFragment())
                .addToBackStack("").commit()
        }
    }

    private fun startSetting() {
        binding.ivMainSetting.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, SettingFragment())
                .addToBackStack("").commit()
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
