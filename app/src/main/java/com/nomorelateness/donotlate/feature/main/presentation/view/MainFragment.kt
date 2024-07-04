package com.nomorelateness.donotlate.feature.main.presentation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.presentation.CurrentUser
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
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding
        get() = _binding!!

    private val mainPageViewModel: MainPageViewModel by viewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getCurrentUserDataUseCase,
        )
    }

    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        firstLoading()

        mainPageViewModel.getCurrentUserData()
//
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getCurrentUserData()

        observeViewModel()
        initButton()
        setTextUserName()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null


    }

    private var backPressedTime = 0L


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

    private fun setTextUserName() {
        if (CurrentUser.userData?.name != null) {
            binding.tvMainTitle.text = CurrentUser.userData?.name
        }
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

    //딜레이로 로딩 넣어두기
    private fun firstLoading() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val sharedPrefValue = resources.getString(R.string.preference_loading_key)
        val loadingValue =
            sharedPref.getString(getString(R.string.preference_loading_key), sharedPrefValue)

        if (loadingValue == "1") {
            with(sharedPref.edit()) {
                putString(getString(R.string.preference_loading_key), "2")
                apply()
            }

            binding.constraint.visibility = View.GONE
            binding.constraint2.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.shimmerLayout.visibility = View.GONE
                binding.shimmerLayout2.visibility = View.GONE
                binding.constraint.visibility = View.VISIBLE
                binding.constraint2.visibility = View.VISIBLE
            }, 1000)
        } else {
            binding.shimmerLayout.visibility = View.GONE
            binding.shimmerLayout2.visibility = View.GONE
            binding.constraint.visibility = View.VISIBLE
            binding.constraint2.visibility = View.VISIBLE
        }
    }
}
