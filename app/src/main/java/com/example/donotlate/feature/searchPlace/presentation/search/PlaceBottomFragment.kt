package com.example.donotlate.feature.searchPlace.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.MainActivity
import com.example.donotlate.databinding.FragmentPlaceBottomBinding
import com.example.donotlate.feature.searchPlace.domain.adapter.MapAdapter
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import com.example.donotlate.feature.searchPlace.presentation.detail.PlaceDetailFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class PlaceBottomFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentPlaceBottomBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            PlaceSearchViewModel.SearchViewModelFactory()
        )[PlaceSearchViewModel::class.java]
    }

    private lateinit var mapAdapter : MapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceBottomBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapAdapter = MapAdapter()


        initViewModel()
        initMapList()

    }

    private fun initViewModel() {

        searchViewModel.searchMapList.observe(viewLifecycleOwner) {
            mapAdapter.itemList = it
            with(binding.rvMap) {
                adapter = mapAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun initMapList() {

        mapAdapter.setOnItemClickListener(object : MapAdapter.OnItemClickListener {
            override fun onItemClick(mapData: PlaceModel) {
                arguments?.putParcelable("data", mapData)
                val activity = activity as MainActivity
                activity.addFragment(PlaceDetailFragment())
            }
        })

        searchViewModel.searchMapList.observe(viewLifecycleOwner) { map ->
            mapAdapter.setItem(map)
        }
        fetchMap()
    }

    private fun fetchMap() {
        searchViewModel.getData().observe(viewLifecycleOwner, Observer {
            searchViewModel.getSearchMapList(it)
        })
    }
}
