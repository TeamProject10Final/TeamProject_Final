package com.example.donotlate.feature.mypromise.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.databinding.FragmentMypromiseListBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.example.donotlate.feature.mypromise.presentation.adapter.MyPromiseAdapter
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class MyPromiseListFragment : Fragment() {

    private val myPromiseListViewModel: MyPromiseListViewModel by viewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseListViewModelFactory(
            appContainer.loadToMyPromiseListUseCase
        )
    }

    private var _binding: FragmentMypromiseListBinding? = null
    private val binding: FragmentMypromiseListBinding
        get() = _binding!!

    private val currentUser = CurrentUser.userData


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypromiseListBinding.inflate(inflater, container, false)
        setPromiseList()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backButton()
        observeViewModel()




        val adapter = MyPromiseAdapter { promiseRoom ->
            openPromiseRoomFragment(promiseRoom)
        }
        binding.rvMyPromise.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvMyPromise.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseListViewModel.promiseRoomModel.collect { promiseRooms ->
                adapter.submitList(promiseRooms)

                if (promiseRooms.isEmpty()) {
                    binding.rvMyPromise.isVisible = false
                    binding.tvDefaultCenter.isVisible = true
                } else {
                    binding.rvMyPromise.isVisible = true
                    binding.tvDefaultCenter.isVisible = false
                }
            }
        }


        val currentTime: Long = System.currentTimeMillis()
        val dataFormat1 = SimpleDateFormat("yyyy-MM-dd")
        binding.tvTitleDate.text = dataFormat1.format(currentTime).toString()

    }

    private fun setPromiseList() {
        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseListViewModel.loadPromiseRooms()
        }
    }

    private fun openPromiseRoomFragment(roomInfo: PromiseModel) {
        val fragment = MyPromiseRoomFragment()
        val bundle = Bundle()
        bundle.putParcelable("promiseRoom", roomInfo)
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .setCustomAnimations(
                /* enter = */ R.anim.slide_in,
                /* exit = */ R.anim.fade_out,
            )
            .add(R.id.frame, fragment)
            .addToBackStack(null)
            .commit()

    }

    private fun backButton() {
        binding.ivPromiseBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, MainFragment())
                .commit()
        }
    }

    fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            myPromiseListViewModel.closestPromiseTitle.collect { title ->
                binding.tvTitleName.text = title
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentFragmentManager.popBackStack()
        _binding = null
    }
}
