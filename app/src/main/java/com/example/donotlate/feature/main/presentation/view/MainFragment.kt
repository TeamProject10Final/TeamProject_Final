package com.example.donotlate.feature.main.presentation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMainBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity
import com.example.donotlate.feature.friends.presentation.view.FriendsActivity
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
            appContainer.getCurrentUserDataUseCase,
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
        super.onCreate(savedInstanceState)
        Log.d("MainFragment", "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        mainPageViewModel.getCurrentUserData()
        Log.d("MainFragment", "onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("MainFragment", "onViewCreated")
//        getCurrentUserData()

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
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out,
                )
                .replace(R.id.frame, PlaceSearchFragment())
                .addToBackStack(null).commit()

        }
    }

    private fun startSetting() {
        binding.ivMainSetting.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out,
                )
                .replace(R.id.frame, SettingFragment())
                .addToBackStack(null).commit()

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
            mainPageViewModel.currentUserData.collect { userData ->
                if (userData != null) {
                    binding.tvMainTitle.text = userData.name
                }
            }
        }
    }

    private fun startMiniGame() {
        binding.layoutMainGame.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out,
                )
                .replace(R.id.frame, MiniGameFragment())
                .addToBackStack(null).commit()

        }
    }

    private fun startMyPromise() {
        binding.layoutMainReservation.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out,
                )
                .replace(R.id.frame, MyPromiseListFragment())
                .addToBackStack(null).commit()

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainFragment", "onStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainFragment", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainFragment", "onStop")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainFragment", "onResume")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("MainFragment", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainFragment", "onDestroy")
    }
}
