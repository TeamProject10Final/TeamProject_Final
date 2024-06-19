package com.example.donotlate.feature.setting.presentation.view.question

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSettingEmailBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment


class SettingEmailFragment : Fragment() {

    private var _binding: FragmentSettingEmailBinding? = null
    private val binding get() = _binding!!


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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun sendEmail() {
//        val intent = Intent(Intent.ACTION_SENDTO).apply {
//            type = "text/plain"
//            putExtra(Intent.EXTRA_EMAIL, "sp10nomorelateness@gmail.com")
//        }
//        requireActivity().startActivity(Intent.createChooser(intent, "메일 전송하기"))
//    }

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