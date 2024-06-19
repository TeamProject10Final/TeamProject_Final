package com.example.donotlate.feature.setting.presentation.view.information

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentSettingInformationPersonalBinding
import com.example.donotlate.databinding.FragmentSettingLicenseBinding
import com.example.donotlate.feature.setting.presentation.adapter.LicenseAdapter
import com.example.donotlate.feature.setting.presentation.view.model.LicenseModel
import com.example.donotlate.feature.setting.presentation.view.viewmodel.SettingViewModel

class SettingLicenseFragment : Fragment() {

    private var _binding: FragmentSettingLicenseBinding? = null
    private val binding get() = _binding!!

    private lateinit var licenseAdapter: LicenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSettingLicenseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        backButton()
    }

    private fun backButton() {
        binding.ivSettingInfoLicenseBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, SettingInformationFragment())
                .commit()
        }
    }

    private fun initView() {
        val viewModel = SettingViewModel()
        val data = viewModel.licenseList()

        licenseAdapter = LicenseAdapter()
        licenseAdapter.itemList = data

        with(binding.rvLicense) {
            adapter = licenseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        licenseAdapter.itemClick = object : LicenseAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                var intent = Intent(Intent.ACTION_VIEW, Uri.parse(data[position].uri))
                requireActivity().startActivity(intent)
            }

        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}