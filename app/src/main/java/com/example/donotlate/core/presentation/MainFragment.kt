package com.example.donotlate.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMainBinding
import com.example.donotlate.feature.room.presentation.main.ViewPagerFragment
import com.example.donotlate.feature.room.presentation.dialog.LogoutFragmentDialog
import com.example.donotlate.map.SearchPlaceFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment() {

    private lateinit var binding : FragmentMainBinding

    private var param1: String? = null
    private var param2: String? = null
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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
        placeButton()
        startPlace()

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
            activity.changeFragment(SearchPlaceFragment())
        }
    }

    private fun logoutButton() {
        binding.ivMainLogout.setOnClickListener {
            val dialog = LogoutFragmentDialog()
            dialog.show(requireActivity().supportFragmentManager, "BackFragmentDialog")
            //firebase 로그아웃 기능 추가
        }
    }

    private fun startPlace(){
        binding.layoutMainPlace.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.frame, SearchPlacesFragment()).addToBackStack("").commit()
        }
    }
}