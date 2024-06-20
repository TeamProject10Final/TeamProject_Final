package com.example.donotlate.feature.searchPlace.presentation.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentPlaceSearchBinding
import com.example.donotlate.feature.consumption.presentation.ConsumptionActivity
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.example.donotlate.feature.searchPlace.presentation.adapter.MapAdapter
import com.example.donotlate.feature.searchPlace.presentation.detail.PlaceDetailFragment
import com.example.donotlate.feature.searchPlace.presentation.mapper.PlaceModel


class PlaceSearchFragment : Fragment() {

    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding: FragmentPlaceSearchBinding
        get() = _binding!!


    private lateinit var mapAdapter: MapAdapter


    private val searchViewModel: PlaceSearchViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        PlaceSearchViewModelFactory(
            appContainer.getSearchListUseCase
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapAdapter = MapAdapter()


        binding.btnSearchButton.setOnClickListener {
            initMapList()
            initViewModel()
            fetchMap()
            hideKeyboard(view)

            binding.etSearchBox.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    binding.etSearchBox.clearFocus()
                    ConsumptionActivity.hideKeyboard(view)
                }
            }
        }

        hideKey(view)
        backButton()
    }

    private fun backButton() {
        binding.ivPlaceBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, MainFragment())
                .addToBackStack("MainFragment")
                .commit()
        }
    }

    private fun fetchMap() {
        val query = binding.etSearchBox.text.toString()
        if (query.trim().isEmpty()) {
            binding.imageView2.isVisible = true
            binding.tvDefaultText.isVisible = true
        } else {
            binding.rvMap.isVisible = true
            binding.imageView2.isVisible = false
            binding.tvDefaultText.isVisible = false
            searchViewModel.updateText(query)
        }
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
                val fragment = PlaceDetailFragment()
                val bundle = Bundle()
                bundle.putParcelable("data", mapData)
                fragment.arguments = bundle
                Log.d("debug2", "${mapData}")
                parentFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        /* enter = */ R.anim.slide_in,
                        /* exit = */ R.anim.fade_out,
                    )
                    .replace(R.id.fg_Search, fragment) //replace는 교체, add는 추가
                    .addToBackStack("PlaceDetailFragment")
                    .commit()
            }
        })
        searchViewModel.searchMapList.observe(viewLifecycleOwner) { map ->
            mapAdapter.setItem(map)
        }
        fetchMapList()
    }

    private fun fetchMapList() {
        searchViewModel.getData().observe(viewLifecycleOwner, Observer {
            searchViewModel.getSearchMapList(it)
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKey(view: View) {
        //바깥 터치 시 키보드 숨기는 부분...
        binding.root.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                ConsumptionActivity.hideKeyboard(view)
                binding.etSearchBox.clearFocus()
            }
            false
        }
    }

    private fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

//    private fun clickChip() {
//        binding.cgChipGroup.setOnCheckedStateChangeListener { chipGroup, ints ->
//            val selectChip = chipGroup.checkedChipId
//            document = selectChip
//            when (selectChip) {
//                R.id.tv_restaurant -> {
//                    getChipGroupType(ChipType.RESTAURANT)
//                }
//
//                R.id.tv_cafe -> {
//                    getChipGroupType(ChipType.CAFE)
//                }
//
//                R.id.tv_cinema -> {
//                    getChipGroupType(ChipType.MOVIETHEATER)
//                }
//
//                R.id.tv_park -> {
//                    getChipGroupType(ChipType.PARK)
//                }
//
//                R.id.tv_shoppingMall -> {
//                    getChipGroupType(ChipType.SHOPPINGMALL)
//                }
//            }
//        }
//    }

//    private fun getChipGroupType(type: ChipType) {
//
//        if (binding.etSeachBox.text.isEmpty()) {
//            binding.tvDefaultText.isVisible = true
//        } else {
//            binding.tvDefaultText.isVisible = false
//
//        }
//
//        when (type) {
//            ChipType.RESTAURANT -> {
//                viewModel.getSearchType(location = "", types = "restaurant")
//            }
//
//            ChipType.CAFE -> {
//                viewModel.getSearchType(location = "", types = "cafe")
//            }
//
//            ChipType.MOVIETHEATER -> {
//                viewModel.getSearchType(location = "", types = "movieTheater")
//            }
//
//            ChipType.PARK -> {
//                viewModel.getSearchType(location = "", types = "park")
//            }
//
//            ChipType.SHOPPINGMALL -> {
//                viewModel.getSearchType(location = "", types = "shoppingMall")
//            }
//        }
//
//        바텀시트와 연결
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}