package com.nomorelateness.donotlate.feature.room.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nomorelateness.donotlate.databinding.FragmentRoomMapBottomBinding
import com.nomorelateness.donotlate.feature.searchPlace.presentation.adapter.MapAdapter
import com.nomorelateness.donotlate.feature.searchPlace.presentation.mapper.PlaceModel

class RoomMapBottomFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentRoomMapBottomBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer =
            (requireActivity().application as com.nomorelateness.donotlate.DoNotLateApplication).appContainer
        RoomViewModelFactory(
            appContainer.getSearchListUseCase,
            appContainer.makeAPromiseRoomUseCase,
            appContainer.getFriendsListFromFirebaseUseCase,
        )
    }

    private lateinit var mapAdapter: MapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomMapBottomBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapAdapter = MapAdapter()

        initView()
        initMapList()

    }

    private fun initView() {
        roomViewModel.searchMapList.observe(viewLifecycleOwner) {
            mapAdapter.itemList = it
            with(binding.rvRoomMapBottom) {
                adapter = mapAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }
    }

    private fun initMapList() {

        mapAdapter.setOnItemClickListener(object : MapAdapter.OnItemClickListener {
            override fun onItemClick(mapData: PlaceModel) {
                mapData.let {
                    roomViewModel.setMapData(it)
                    Log.d("확인 setMapData", "${it}")
                }
//sdk 33
//                val fragment = RoomMapFragment()
//                val bundle = Bundle()
//                bundle.putParcelable("data", mapData)
//                Log.d("debug3", "${mapData}")
//                fragment.arguments = bundle
//                requireActivity().supportFragmentManager
//                    .beginTransaction().add(fragment, "tag")
//                    .commit()
//
                dismiss()
            }
        })

        roomViewModel.searchMapList.observe(viewLifecycleOwner) { map ->
            mapAdapter.setItem(map)
        }
        fetchMap()
    }

    private fun fetchMap() {
        roomViewModel.getQuery().observe(viewLifecycleOwner, Observer {
            roomViewModel.getSearchMapList(it)
        })
    }


}