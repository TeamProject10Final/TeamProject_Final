package com.example.donotlate.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.databinding.FragmentSearchPlaceDetailBinding

class SearchPlaceDetailFragment : Fragment() {


    private var _binding: FragmentSearchPlaceDetailBinding? = null
    private val binding: FragmentSearchPlaceDetailBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchPlaceDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rbRatingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
        //레이팅바 연결
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}