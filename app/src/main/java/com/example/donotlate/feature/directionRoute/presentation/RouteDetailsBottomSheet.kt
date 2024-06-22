package com.example.donotlate.feature.directionRoute.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.donotlate.AppContainer
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentRouteDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RouteDetailsBottomSheet : BottomSheetDialogFragment() {
    private var _binding: FragmentRouteDetailsBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(
            "확인",
            "값들 : ${sharedViewModel.origin.value.toString()}, ${sharedViewModel.destination.value.toString()},${sharedViewModel.mode.value.toString()}"
        )

//        binding.routeDetailsText.text = sharedViewModel.directionExplanations.value
        sharedViewModel.directionExplanations.observe(viewLifecycleOwner) {
            binding.routeDetailsText.text = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}