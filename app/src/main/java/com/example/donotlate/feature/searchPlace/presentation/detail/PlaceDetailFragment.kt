package com.example.donotlate.feature.searchPlace.presentation.detail

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.example.donotlate.MainActivity
import com.example.donotlate.databinding.FragmentPlaceDetailBinding
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel


class PlaceDetailFragment : Fragment() {


    private var _binding : FragmentPlaceDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            PlaceDetailViewModel.PlaceDetailViewModelFactory()
        )[PlaceDetailViewModel::class.java]
    }

    private var data : PlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            data = it.getParcelable("data")
            if (data != null) {
                detailViewModel.setSelectedItem(data!!)
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()

    }

    private fun initView() {
        
        detailViewModel.data.observe(viewLifecycleOwner) {
            if (it != null) {
                with(binding) {
                    ivPlaceDetailTitle.load(it.img)
                    tvPlaceDetailTitle.text = it.name
                    tvPlaceDetailAddress.text = it.address
                    tvPlaceDetailPhoneNumber.text = it.phoneNumber
                }
            }
        }
    }
}