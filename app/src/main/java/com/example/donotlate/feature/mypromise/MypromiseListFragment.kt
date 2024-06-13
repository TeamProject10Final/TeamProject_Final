package com.example.donotlate.feature.mypromise

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentMypromiseListBinding
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModel
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class MypromiseListFragment : Fragment() {

    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getUserDataUseCase,
            appContainer.getAllUsersUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.imageUploadUseCase
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivPromiseBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    //if (//약속이 없는지 감지){
    // binding.rvMyPromise.isVisible = false
    // binding.tvDefaultCenter.isVisible = true
    // } else {
    // binding.rvMyPromise.isVisible = true
    // binding.tvDefaultCenter.isVisible = false
    // }

        val currentTime : Long = System.currentTimeMillis()
        val dataFormat1 = SimpleDateFormat("yyyy-MM-dd")
        binding.tvTitleDate.text = dataFormat1.format(currentTime).toString()

        observeViewModel()

    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainPageViewModel.getUserData.collect { result ->
                result?.onSuccess { myInfo ->
                    binding.tvTopUserName.text = myInfo.name
                    Log.d("observeViewModel", "${myInfo.name}")
                }?.onFailure { e ->
                    throw e
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}