package com.example.donotlate.feature.searchPlace.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.donotlate.MainActivity
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentPlaceDetailBinding
import com.example.donotlate.feature.searchPlace.domain.adapter.MapAdapter
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import com.example.donotlate.feature.searchPlace.presentation.viewmodel.SearchViewModel


class PlaceDetailFragment : Fragment() {

    private var _binding : FragmentPlaceDetailBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

}