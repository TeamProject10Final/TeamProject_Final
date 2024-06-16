package com.example.donotlate.feature.mypromise.presentation.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMypromiseListBinding
import com.example.donotlate.feature.main.presentation.view.MainFragment
import com.example.donotlate.feature.mypromise.presentation.adapter.MyPromiseAdapter
import com.example.donotlate.feature.mypromise.presentation.model.PromiseModel
import com.example.donotlate.feature.mypromise.presentation.viewmodel.MyPromiseViewModel
import com.example.donotlate.feature.mypromise.presentation.viewmodel.MyPromiseViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class MyPromiseListFragment : Fragment() {

//    private val mainPageViewModel: MainPageViewModel by activityViewModels {
//        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
//        MainPageViewModelFactory(
//            appContainer.getUserDataUseCase,
//            appContainer.getAllUsersUseCase,
//            appContainer.getCurrentUserUseCase,
//            appContainer.imageUploadUseCase
//        )
//    }

    private val myPromiseViewModel: MyPromiseViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MyPromiseViewModelFactory(
            appContainer.loadToMyPromiseListUseCase,
            appContainer.messageSendingUseCase,
            appContainer.messageReceivingUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.getUserDataUseCase,
            appContainer.getMyDataFromFirebaseUseCase,
            appContainer.firebaseAuth,
            appContainer.getDirectionsUseCase
        )
    }

    private var _binding: FragmentMypromiseListBinding? = null
    private val binding: FragmentMypromiseListBinding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypromiseListBinding.inflate(inflater, container, false)
        lifecycleScope.launch {
            myPromiseViewModel.getCurrentUserId()
        }
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

        lifecycleScope.launch {
            myPromiseViewModel.promiseRoomModel.collect { promiseRooms ->
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

    private fun setPromiseList(uid: String) {
        lifecycleScope.launch {
            myPromiseViewModel.loadPromiseRooms(uid)
        }
    }

    private fun openPromiseRoomFragment(roomInfo: PromiseModel) {
        val fragment = MyPromiseRoomFragment()
        val bundle = Bundle()
        bundle.putParcelable("promiseRoom", roomInfo)
        fragment.arguments = bundle

        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.frame, fragment)
            .addToBackStack(null)
            .commit()

    }

    private fun backButton() {
        binding.ivPromiseBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame, MainFragment())
                .commit()
        }
    }

    fun observeViewModel() {
        lifecycleScope.launch {
            myPromiseViewModel.currentUserId.collect { uid ->
                if (uid != null) {
                    Log.d("asdasd", "$uid")
                    setPromiseList(uid)
                }
            }
        }
        lifecycleScope.launch {
            myPromiseViewModel.currentUserData.collect{userData ->
                userData?.let {
                    binding.tvTopUserName.text = userData.name ?: throw NullPointerException("User Data Null")
                }
            }
        }
        lifecycleScope.launch {
            myPromiseViewModel.closestPromiseTitle.collect{title ->
                binding.tvTitleName.text = title
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}