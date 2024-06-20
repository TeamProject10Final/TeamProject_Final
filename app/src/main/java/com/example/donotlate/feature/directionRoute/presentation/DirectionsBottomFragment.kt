package com.example.donotlate.feature.directionRoute.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.AppContainer
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentDirectionsBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class DirectionsBottomFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDirectionsBottomBinding? = null
    private val binding get() = _binding!!

    private val appContainer: AppContainer by lazy {
        (requireActivity().application as DoNotLateApplication).appContainer
    }

    // DirectionsViewModel1Factory 가져오기
    private val directionsViewModel1Factory: DirectionsViewModel1Factory by lazy {
        appContainer.directions1Container.directionsViewModel1Factory
            ?: throw IllegalStateException("DirectionsViewModel1Factory not initialized properly")
    }

    // SharedViewModel 가져오기
    private val sharedViewModel: DirectionsViewModel1 by activityViewModels { directionsViewModel1Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var directionsAdapter: DirectionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDirectionsBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        directionsAdapter = DirectionsAdapter()

        initData()
        initClick()

    }

    private fun initData() {

        sharedViewModel.routeSelectionText.observe(viewLifecycleOwner) {
            directionsAdapter.itemList = it
            with(binding.recyclerView) {
                adapter = directionsAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun initClick() {
        directionsAdapter.itemClick = object : DirectionsAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
//                val selectedIndex = binding.etSelectionIndex.text.toString()
//                if (selectedIndex != "") {
//                    val thisIndex = selectedIndex.toInt()
//                    sharedViewModel.setSelectedRouteIndex(thisIndex)
//                    if (sharedViewModel.mode.value.toString() == "transit") {
//                        Log.d("확인 화살표", "${sharedViewModel.mode.value.toString()}")
//                        binding.ivDetailView.isVisible = true
//                    } else {
//                        Log.d("확인 화살표 else", "${sharedViewModel.mode.value.toString()}")
//                        binding.ivDetailView.isVisible = false
//                    }
//                } else {
//                    Log.d("확인 인덱스 오류", "$selectedIndex")
//                }
                val selectedIndex = position
                if (position != null) {
                    sharedViewModel.setSelectedRouteIndex(position)
                    if (sharedViewModel.mode.value.toString() == "transit") {
                        Log.d("확인 화살표", "${sharedViewModel.mode.value.toString()}")
                    } else {
                        Log.d("확인 화살표 else", "${sharedViewModel.mode.value.toString()}")
                    }
                } else {
                    Log.d("확인 인덱스 오류", "$selectedIndex")
                }
                sharedViewModel.afterSelecting()
                dismiss()
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}