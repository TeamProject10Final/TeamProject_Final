package com.nomorelateness.donotlate.feature.setting.presentation.view.question

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentSettingChatBinding


class SettingChatFragment : Fragment() {

    private var _binding: FragmentSettingChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingChatBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        backButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun backButton() {
        binding.ivSettingChatBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, SettingQuestionFragment())
                .commit()
        }
    }

    private fun initView() {
        binding.clSettingChatKakao.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://open.kakao.com/o/g5qJY8xg"))
            requireActivity().startActivity(intent)
        }

        binding.clSettingChatDiscord.setOnClickListener {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/CTQaZPnG"))
            requireActivity().startActivity(intent)
        }
    }
}