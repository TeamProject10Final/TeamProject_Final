package com.nomorelateness.donotlate.feature.setting.presentation.view.question

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.presentation.CurrentUser
import com.nomorelateness.donotlate.databinding.FragmentSettingEmailBinding


class SettingEmailFragment : Fragment() {

    private var _binding: FragmentSettingEmailBinding? = null
    private val binding get() = _binding!!

    private val userData = CurrentUser.userData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton()

        binding.tvSettingQName.text = userData?.name

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun backButton() {
        binding.ivSettingEmailBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, SettingQuestionFragment())
                .commit()
        }
    }
}