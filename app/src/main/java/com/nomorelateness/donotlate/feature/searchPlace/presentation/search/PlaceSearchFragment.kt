package com.nomorelateness.donotlate.feature.searchPlace.presentation.search

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
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.core.presentation.util.UtilityKeyboard.UtilityKeyboard.hideKeyboard
import com.nomorelateness.donotlate.databinding.FragmentPlaceSearchBinding
import com.nomorelateness.donotlate.feature.main.presentation.view.MainFragment
import com.nomorelateness.donotlate.feature.searchPlace.presentation.adapter.MapAdapter
import com.nomorelateness.donotlate.feature.searchPlace.presentation.detail.PlaceDetailFragment
import com.nomorelateness.donotlate.feature.searchPlace.presentation.mapper.PlaceModel


class PlaceSearchFragment : Fragment() {

    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding: FragmentPlaceSearchBinding
        get() = _binding!!

    private lateinit var mapAdapter: MapAdapter


    private val searchViewModel: PlaceSearchViewModel by activityViewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}