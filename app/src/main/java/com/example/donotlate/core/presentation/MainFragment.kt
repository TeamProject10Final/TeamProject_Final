package com.example.donotlate.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.MainActivity
import com.example.donotlate.MiniGameFragment
import com.example.donotlate.MypromiseListFragment
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMainBinding
import com.example.donotlate.feature.room.presentation.dialog.LogoutFragmentDialog
import com.example.donotlate.feature.room.presentation.main.ViewPagerFragment
import com.example.donotlate.feature.searchPlace.presentation.SearchPlacesFragment
import com.example.donotlate.feature.setting.SettingFragment


class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding
    private var name: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        name = "홍길동"
        binding.tvMainTitle.text = name

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        startRoom()
        logoutButton()
        startSearchPlace()
        startMyPromise()
        startMiniGame()


        placeButton()
        startPlace()
        startSetting()

    }

    private fun startRoom() {
        binding.layoutMainRoom.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(ViewPagerFragment())
        }
    }

    private fun placeButton() {
        binding.layoutMainPlace.setOnClickListener {
            val activity = activity as MainActivity
            activity.changeFragment(SearchPlacesFragment())
        }
    }

    private fun logoutButton() {
        binding.ivMainLogout.setOnClickListener {
            val dialog = LogoutFragmentDialog()
            dialog.show(requireActivity().supportFragmentManager, "BackFragmentDialog")
            //firebase 로그아웃 기능 추가
        }
    }

    private fun startSearchPlace(){
        binding.layoutMainPlace.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, SearchPlacesFragment()).addToBackStack("").commit()
        }
    }

    private fun startMyPromise(){
        binding.layoutMainReservation.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, MypromiseListFragment()).addToBackStack("").commit()
        }
    }

    private fun startMiniGame(){
        binding.layoutMainGame.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, MiniGameFragment()).addToBackStack("").commit()
        }
    }

    private fun startSetting(){
        binding.ivMainSetting.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, SettingFragment()).addToBackStack("").commit()
        }
    }
}