package com.example.donotlate.feature.searchPlace.presentation.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.example.donotlate.databinding.FragmentPlaceSearchBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.example.donotlate.feature.searchPlace.presentation.adapter.MapAdapter
import com.example.donotlate.feature.searchPlace.presentation.detail.PlaceDetailFragment
import com.example.donotlate.feature.searchPlace.presentation.mapper.PlaceModel
import com.example.donotlate.feature.setting.presentation.view.dialog.LoadingDialog
import kotlin.concurrent.thread


class PlaceSearchFragment : Fragment() {

    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding: FragmentPlaceSearchBinding
        get() = _binding!!

    private lateinit var mapAdapter: MapAdapter

    private val LOCATION_PERMISSION_REQUEST_CODE = 1000

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

        binding.root.setOnClickListener {
            hideKeyboard(binding.root.windowToken)
            //TODO 여기 오류납니다
//            requireActivity().currentFocus!!.clearFocus()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapAdapter = MapAdapter()

        initMapList()
        initViewModel()

        binding.btnSearchButton.setOnClickListener {
            searchViewModel.getSearchMapList(binding.etSearchBox.text.toString())
            binding.imageView2.visibility = View.INVISIBLE
            binding.rvMap.visibility = View.VISIBLE
            binding.tvDefaultText.visibility = View.INVISIBLE
            hideKeyboard(binding.root.windowToken)


        }
//        searchViewModel.getSearchMapList(binding.etSearchBox.text.toString())
//        binding.rvMap.visibility = View.VISIBLE
//        binding.imageView2.visibility = View.INVISIBLE
//        binding.tvDefaultText.visibility = View.INVISIBLE

        editTextProcess()
        backButton()
    }


    override fun onResume() {
        super.onResume()
        fetchMap()
    }


    private fun editTextProcess() {
        binding.etSearchBox.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(binding.root.windowToken)
                //requireActivity().currentFocus!!.clearFocus()
                handled = true

                searchViewModel.getSearchMapList(binding.etSearchBox.text.toString())
                binding.rvMap.visibility = View.VISIBLE
                binding.imageView2.visibility = View.INVISIBLE
                binding.tvDefaultText.visibility = View.INVISIBLE

            }
            handled
        }
    }

    private fun backButton() {
        binding.ivPlaceBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, MainFragment())
                .commit()
        }
    }

    //검색 결과에 따라 뷰 나타냄
    private fun fetchMap() {
        val query = binding.etSearchBox.text.toString()
        if (query.trim().isNotEmpty()) {
            binding.imageView2.visibility = View.INVISIBLE
            binding.tvDefaultText.visibility = View.INVISIBLE
            binding.rvMap.visibility = View.VISIBLE
        } else {
            binding.imageView2.visibility = View.VISIBLE
            binding.tvDefaultText.visibility = View.VISIBLE
            binding.rvMap.visibility = View.INVISIBLE
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
                    .replace(R.id.frame, fragment) //replace는 교체, add는 추가
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