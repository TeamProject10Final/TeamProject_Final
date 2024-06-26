package com.nomorelateness.donotlate.feature.directionRoute.presentation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.databinding.FragmentDirectionsDialogBinding


class DirectionsDialogFragment : DialogFragment() {

    private var _binding: FragmentDirectionsDialogBinding? = null
    private val binding get() = _binding!!

    private val appContainer: com.nomorelateness.donotlate.AppContainer by lazy {
        (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
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

        _binding = FragmentDirectionsDialogBinding.inflate(inflater, container, false)

        isCancelable = !sharedViewModel.checkTwoCountry()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

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

    override fun onDestroy() {
        super.onDestroy()
    }
}