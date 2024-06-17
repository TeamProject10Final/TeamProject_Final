package com.example.donotlate.feature.setting.presentation.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.donotlate.DoNotLateApplication
import com.example.donotlate.R
import com.example.donotlate.databinding.FragmentMypageBinding
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModel
import com.example.donotlate.feature.main.presentation.viewmodel.MainPageViewModelFactory
import kotlinx.coroutines.launch
import java.io.IOException

class MyPageFragment : Fragment() {
    private val mainPageViewModel: MainPageViewModel by activityViewModels {
        val appContainer = (requireActivity().application as DoNotLateApplication).appContainer
        MainPageViewModelFactory(
            appContainer.getUserDataUseCase,
            appContainer.getAllUsersUseCase,
            appContainer.getCurrentUserUseCase,
            appContainer.imageUploadUseCase,
            appContainer.firebaseAuth

        )
    }

    //갤러리에서 이미지 바꾸기
    private var selectedUri: Uri? = null
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) { //이미지를 선택할 경우
                selectedUri = uri
                Log.d("uri확인", selectedUri.toString())
                mainPageViewModel.updateProfile(uri)
                val imageBitmap = uriToBitmap(requireContext(), uri)//uri -> bitMap으로 변경
                binding.ivProfileImage.setImageBitmap(imageBitmap)
            }
        }

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)

//        setUpProfile()
//
//
//        //뒤로가기
//        binding.ivBack.setOnClickListener {
//            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
//            requireActivity().supportFragmentManager.popBackStack()
//        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        setUpProfile()


        //뒤로가기
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setUpProfile() {
        binding.ivFix.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
//        binding.ivDelete.setOnClickListener {
//            selectedUri = null
//            binding.ivProfileImage.setImageResource(R.drawable.ic_user)
//        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainPageViewModel.getUserData.collect { result ->
                result?.onSuccess { myInfo ->
                    binding.tvName.text = myInfo.name
                    binding.tvEmail.text = myInfo.email
                    binding.tvMyPage.text = myInfo.name + "님의 누적 이력입니다."
                    binding.tvMyPage2.text = myInfo.name + "님 만의 특별한 코멘트를 달아볼까요?"
                    Log.d("dddd", "${myInfo.createdAt}")
                    Log.d("dddd", "${myInfo.profileImgUrl}")

                    if (myInfo.profileImgUrl.isBlank()) {
                        Log.d("dddd2", "${myInfo.profileImgUrl}")
                        // 기본 프로필 이미지를 로드합니다.
                        binding.ivProfileImage.load(R.drawable.ic_user) {
                            Log.d("dddd1", "죽이지마라제발..")
                            crossfade(true)
                            placeholder(R.drawable.ic_add_user)
                            error(R.drawable.ic_calendar)
                            transformations(CircleCropTransformation())
                        }
                    } else {
                        // URL을 통해 프로필 이미지를 로드합니다.
                        binding.ivProfileImage.load(myInfo.profileImgUrl) {
                            crossfade(true)
                            placeholder(R.drawable.ic_user)
                            error(R.drawable.ic_user)
                            transformations(CircleCropTransformation())
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            mainPageViewModel.profileImageUrl.collect { imageUrl ->
                if (imageUrl.isNotBlank()) {
                    binding.ivProfileImage.load(imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_user)
                        error(R.drawable.ic_user)
                        transformations(CircleCropTransformation())
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//val profileImgUrl: String = ""
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)

    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}