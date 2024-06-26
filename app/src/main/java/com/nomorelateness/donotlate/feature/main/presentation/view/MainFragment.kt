package com.nomorelateness.donotlate.feature.main.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentMainBinding
import com.nomorelateness.donotlate.feature.consumption.presentation.ConsumptionActivity
import com.nomorelateness.donotlate.feature.friends.presentation.view.FriendsFragment
import com.nomorelateness.donotlate.feature.minigame.MiniGameFragment
import com.nomorelateness.donotlate.feature.mypromise.presentation.view.MyPromiseListFragment
import com.nomorelateness.donotlate.feature.room.presentation.view.ViewPagerFragment
import com.nomorelateness.donotlate.feature.searchPlace.presentation.search.PlaceSearchFragment
import com.nomorelateness.donotlate.feature.setting.presentation.view.SettingFragment
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val mainPageViewModel: MainPageViewModel by viewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getCurrentUserDataUseCase,
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
        initDarMode()
    }

    private fun startRoom() {
        binding.layoutMainRoom.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out,
                )
                .replace(R.id.frame, ViewPagerFragment())
                .addToBackStack(null).commit()
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

    private fun initDarMode(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return

        val sharedPrefValue = resources.getString(R.string.preference_file_key)
        val darkModeValue = sharedPref.getString(getString(R.string.preference_file_key), sharedPrefValue)

        if (darkModeValue == "darkModeOn"){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun startPlace() {

        binding.layoutMainPlace.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out,
                )
                .replace(R.id.frame, PlaceSearchFragment())
                .addToBackStack("PlaceSearchFragment")
                .commit()
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
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.slide_in,
                    /* exit = */ R.anim.fade_out,
                )
                .replace(R.id.frame, FriendsFragment())
                .addToBackStack(null).commit()

        }
//  FriendsActivity 삭제 시, 아래 코드 사용
//        binding.layoutMainGame.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .setCustomAnimations(
//                    /* enter = */ R.anim.slide_in,
//                    /* exit = */ R.anim.fade_out,
//                )
//                .replace(R.id.frame, FriendsFragment())
//                .addToBackStack(null).commit()
//
//        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
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
                .addToBackStack("MiniGameFragment").commit()

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
}
