package com.nomorelateness.donotlate.feature.setting.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.donotlate.R
import com.example.donotlate.core.presentation.CurrentUser
import com.example.donotlate.databinding.FragmentMypageBinding

class MyPageFragment : Fragment() {
//    갤러리에서 이미지 바꾸기
//    private var selectedUri: Uri? = null
//    private val pickMedia =
//        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
//            if (uri != null) { //이미지를 선택할 경우
//                selectedUri = uri
//                Log.d("uri확인", selectedUri.toString())
//                mainPageViewModel.updateProfile(uri)
//                val imageBitmap = uriToBitmap(requireContext(), uri)//uri -> bitMap으로 변경
//                binding.ivProfileImage.setImageBitmap(imageBitmap)
//            }
//        }

    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    private val userData = CurrentUser.userData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProfileData()

        //뒤로가기
        binding.ivBack.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    /* enter = */ R.anim.fade_in,
                    /* exit = */ R.anim.slide_out
                )
                .replace(R.id.frame, SettingFragment())
                .commit()
        }
    }

/*    프로필 이미지 변경
    private fun setUpProfile() {
        binding.ivFix.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.ivDelete.setOnClickListener {
            selectedUri = null
            binding.ivProfileImage.setImageResource(R.drawable.ic_user)
        }*/

    private fun setProfileData() {
        if (userData != null) {
            binding.tvName.text = userData.name
            binding.tvEmail.text = userData.email
            binding.tvMyPage.text = userData.name + "님의 누적 이력입니다."
            binding.tvMyPage2.text = userData.name + "님 만의 특별한 코멘트를 달아볼까요?"

/*          프로필 이미지 변경
            if (userData.profileImgUrl.isBlank()) {
                Log.d("dddd2", "${userData.profileImgUrl}")
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
                binding.ivProfileImage.load(userData.profileImgUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_user)
                    error(R.drawable.ic_user)
                    transformations(CircleCropTransformation())
                }
            }*/
        }

/*      프로필 이미지 변경
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
        }*/
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/* 프로필 이미지 변경
val profileImgUrl: String = ""
fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)

    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}*/
