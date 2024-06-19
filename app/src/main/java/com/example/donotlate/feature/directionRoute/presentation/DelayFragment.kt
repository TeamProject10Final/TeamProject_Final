package com.example.donotlate.feature.directionRoute.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.donotlate.AppContainer
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentDelayBinding
import com.example.donotlate.databinding.FragmentMapBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class DelayFragment : Fragment() {

    lateinit var currentUserLocation: String
    lateinit var currentDestination: String

    private var _binding: FragmentDelayBinding? = null
    private val binding get() = _binding!!

    private val appContainer: AppContainer by lazy {
        (requireActivity().application as DoNotLateApplication).appContainer
    }

    // DirectionsViewModel1Factory 가져오기
    private val directionsViewModel1Factory: DirectionsViewModel1Factory by lazy {
        appContainer.directions1Container.directionsViewModel1Factory
    }

    // SharedViewModel 가져오기
    private val sharedViewModel: DirectionsViewModel1 by activityViewModels { directionsViewModel1Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            //currentUserLocation = it.getString("currentUserLocation").toString()
            currentDestination = it.getString("destination").toString()
        }

        sharedViewModel.setDestination(currentDestination)

        //searchDirections()
    }

//    private fun searchDirections() {
//        var origin = currentDestination
//        if (origin != null) {
//            var destination = currentDestination
//            val mode = "driving"//수정하기
//            sharedViewModel.getDirections(origin, destination, mode)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDelayBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MapFragment())
            .commit()
    }

}
