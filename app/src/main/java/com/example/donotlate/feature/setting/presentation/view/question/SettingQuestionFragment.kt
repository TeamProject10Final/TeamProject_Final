package com.example.donotlate.feature.setting.presentation.view.question

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSettingQuestionBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.example.donotlate.feature.setting.presentation.view.SettingFragment

class SettingQuestionFragment : Fragment() {

    private var _binding: FragmentSettingQuestionBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingQuestionBinding.inflate(inflater, container, false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton()

        binding.clSettingQuestion.setOnClickListener {
            initFragment(R.id.frame, SettingEmailFragment())
        }

        binding.clSettingChat.setOnClickListener {
            initFragment(R.id.frame, SettingChatFragment())
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initFragment(view: Int, fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                /* enter = */ R.anim.slide_in,
                /* exit = */ R.anim.fade_out
            )
            .replace(view, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun backButton() {
        binding.ivSettingQuestionBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, SettingFragment())
                .commit()
        }
    }
}