package com.example.donotlate.feature.auth.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentInformationDialogBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.auth.presentation.view.SignupFragment
import com.example.donotlate.feature.setting.presentation.view.viewmodel.SettingViewModel
import java.time.Year
import java.util.Calendar


class InformationDialogFragment : DialogFragment() {

    private var _binding: FragmentInformationDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCancelable = true

        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInformationDialogBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInfoCancel.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, LoginFragment())
                .commit()
            Toast.makeText(requireContext(), "개인정보 수집 거부되었습니다.", Toast.LENGTH_SHORT).show()
            dismiss()
        }

        binding.btnInfoConfirm.setOnClickListener {
            dismiss()

            viewModel = SettingViewModel()
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val data = "${year}.${month}.${day}"
            viewModel.showDate(data)

            Toast.makeText(requireContext(), "${year}.${month+1}.${day}\n개인정보 수집 동의되었습니다.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}