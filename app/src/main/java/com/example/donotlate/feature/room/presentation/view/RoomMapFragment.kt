package com.example.donotlate.feature.room.presentation.view

import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.databinding.FragmentRoomMapBinding
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModel
import com.example.donotlate.feature.room.presentation.viewmodel.RoomViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RoomMapFragment : Fragment() {

    private var _binding: FragmentRoomMapBinding? = null
    private val binding get() = _binding!!

    private val roomViewModel: RoomViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        RoomViewModelFactory(appContainer.getAllUsersUseCase, appContainer.makeAPromiseRoomUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRoomMapBinding.inflate(inflater, container, false)

        setTitle()

        return binding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d("확인", "onViewStateRestored")

    }

    override fun onResume() {
        super.onResume()

        Log.d("확인", "onResume")

        roomViewModel.getAllUserData()
        lifecycleScope.launch {
            roomViewModel.getAllUserData.collect { userList ->
                userList.forEach { user ->
                    Log.d("User", user.name)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextProcess()

        Log.d("확인", "onViewCreated")
    }

    private fun editTextProcess() {
        binding.etRoomMapSearch.setOnEditorActionListener { textView, action, keyEvent ->
            var handled = false

            if (action == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                requireActivity().currentFocus!!.clearFocus()
                handled = true
            }
            handled
        }
    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setTitle() {
        val title = SpannableStringBuilder("당장 만나,\n목적지를 정해주세요.")
        title.apply {
            setSpan(RelativeSizeSpan(1.4f), 7, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        binding.tvRoomMapTitle.text = title
    }
}