package com.example.donotlate.feature.setting.presentation.view.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSettingPersonalAgreementBinding
import com.example.donotlate.feature.setting.presentation.view.SettingFragment


class SettingPersonalAgreementFragment : Fragment() {

    private var _binding : FragmentSettingPersonalAgreementBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingPersonalAgreementBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        backButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun backButton(){
        //뒤로가기
        binding.ivSettingInfoPersonalBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, SettingInformationFragment())
                .commit()
        }
    }
}