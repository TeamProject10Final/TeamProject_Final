package com.nomorelateness.donotlate.feature.auth.presentation.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentInformationDialogBinding
import com.example.donotlate.feature.auth.presentation.view.LoginFragment
import com.example.donotlate.feature.setting.presentation.view.viewmodel.SettingViewModel
import java.util.Calendar


class InformationDialogFragment : DialogFragment() {

    private var _binding: FragmentInformationDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SettingViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentInformationDialogBinding.inflate(inflater, container, false)

        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCheckBox()
        buttonListener()

    }

    private fun initCheckBox() {

        val allCheckBtn = binding.cbCheckAll
        val useCheckBtn = binding.cbCheckUse
        val perCheckBtn = binding.cbCheckPersonal
        val locCheckBtn = binding.cbCheckLocation
        allCheckBtn.setOnClickListener { onCheckChanged(allCheckBtn) }
        useCheckBtn.setOnClickListener { onCheckChanged(useCheckBtn) }
        perCheckBtn.setOnClickListener { onCheckChanged(perCheckBtn) }
        locCheckBtn.setOnClickListener { onCheckChanged(locCheckBtn) }
    }

    private fun onCheckChanged(compoundButton: CompoundButton) {

        val allCheckBtn = binding.cbCheckAll
        val useCheckBtn = binding.cbCheckUse
        val perCheckBtn = binding.cbCheckPersonal
        val locCheckBtn = binding.cbCheckLocation

        when (compoundButton.id) {
            R.id.cb_Check_All -> {
                if (allCheckBtn.isChecked) {
                    useCheckBtn.isChecked = true
                    perCheckBtn.isChecked = true
                    locCheckBtn.isChecked = true
                } else {
                    useCheckBtn.isChecked = false
                    perCheckBtn.isChecked = false
                    locCheckBtn.isChecked = false
                }
            }

            else -> {
                allCheckBtn.isChecked = (
                        useCheckBtn.isChecked
                                && perCheckBtn.isChecked
                                && locCheckBtn.isChecked
                        )
            }
        }
        if (allCheckBtn.isChecked == true && useCheckBtn.isChecked == true && perCheckBtn.isChecked == true && locCheckBtn.isChecked == true) {
            binding.btnInfoConfirm.isEnabled = true
            binding.btnInfoConfirm.isVisible = true
            binding.btnInfoConfirmFalse.isVisible = false
        } else  {

        }


    }

    private fun buttonListener() {

        binding.btnInfoCancel.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, LoginFragment())
                .commit()
            Toast.makeText(requireContext(), "이용약관 수집 거부되었습니다.", Toast.LENGTH_SHORT).show()
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

            Toast.makeText(
                requireContext(),
                "${year}.${month + 1}.${day}\n이용약관 수집 동의되었습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}