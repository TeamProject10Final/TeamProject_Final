package com.nomorelateness.donotlate.feature.directionRoute.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.gms.maps.model.LatLng
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.FragmentDelayBinding

class DelayFragment : Fragment() {

    lateinit var currentUserLocation: String
    lateinit var currentDestination: String

    private var _binding: FragmentDelayBinding? = null
    private val binding get() = _binding!!

    private val appContainer: com.nomorelateness.donotlate.AppContainer by lazy {
        (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
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
            val currentDestLat = it.getDouble("des lat").toString().toDouble()
            val currentDestLng = it.getDouble("des lng").toString().toDouble()

            sharedViewModel.setDestination(currentDestination)
            val destLatLng = LatLng(currentDestLat, currentDestLng)
            sharedViewModel.setDestLocation(destLatLng)
            val locationUtils = LocationUtils()
            val destCountry = locationUtils.getCountryFromLatLng(
                requireContext(),
                currentDestLat,
                currentDestLng
            )
            destCountry?.let {
                Log.d("확인 도착지 나라", it)
                sharedViewModel.setDesCountry(it)
            }
        }


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