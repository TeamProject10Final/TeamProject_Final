package com.example.donotlate.feature.searchPlace.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.MainActivity
import com.example.donotlate.databinding.FragmentPlaceBottomBinding
import com.example.donotlate.feature.searchPlace.domain.adapter.MapAdapter
import com.example.donotlate.feature.searchPlace.presentation.data.PlaceModel
import com.example.donotlate.feature.searchPlace.presentation.viewmodel.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PlaceBottomFragment : BottomSheetDialogFragment() {

    private var param1: String? = null
    private var param2: String? = null

    private var _binding : FragmentPlaceBottomBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            SearchViewModel.SearchViewModelFactory()
        )[SearchViewModel::class.java]
    }

    private lateinit var mapAdapter : MapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

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
//        initViewModel()
//        initMapList()

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlaceBottomFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    private fun initViewModel() {
//        searchViewModel.searchMapList.observe(viewLifecycleOwner) {
//            mapAdapter.itemList = it
//            with(binding.rvMap) {
//                adapter = mapAdapter
//                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            }
//        }
//    }
//
//    private fun initMapList() {
//
//        mapAdapter.setOnItemClickListener(object : MapAdapter.OnItemClickListener {
//            override fun onItemClick(mapData: PlaceModel) {
//                (requireActivity() as MainActivity).mapData(mapData)
//            }
//        })
//        searchViewModel.searchMapList.observe(viewLifecycleOwner) { map ->
//            mapAdapter.setItem(map)
//        }
//        fetchMap()
//    }
//
//    private fun fetchMap() {
//        setFragmentResultListener("requestKey") { key, bundle ->
//            val result = bundle.getString("bundleKey")
//
//            searchViewModel.getSearchMapList(result!!)
//        }
//    }
}
