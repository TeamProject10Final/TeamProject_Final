package com.example.donotlate.feature.minigame

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.donotlate.databinding.FragmentRouletteBinding

class RouletteFragment : Fragment(), Animation.AnimationListener {

    private var count = 0
    private var flag = false

    private var powerButton: Button? = null
    private var pointerImageView: ImageView? = null
    private var infoText: TextView? = null
    private var prizeText = ""

    private var _binding: FragmentRouletteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouletteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        powerButton = binding.btnGameButton
        powerButton!!.setOnTouchListener(PowerTouchListener())
        intSpinner()
        editTextProcess()
    }

    private fun intSpinner() {
        pointerImageView = binding.ivGameRoulette
        infoText = binding.tvGameInfoText
    }

    private fun startSpinner() {
        val prizes = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8)
        var mSpinDuration: Long = 0
        var mSpinRevolution = 0f

        mSpinRevolution = 3600f
        mSpinDuration = 5000

        if (count >= 30) {
            mSpinDuration = 1000
            mSpinRevolution = (3600 * 2).toFloat()
        }

        if (count >= 60) {
            mSpinDuration = 15000
            mSpinRevolution = (3600 * 3).toFloat()
        }

        val end = Math.floor(Math.random() * 3600).toInt()
        val numOfPrizes = prizes.size
        val shift = 0
        val prizeIndex = (shift + end) % numOfPrizes

        prizeText = "${prizes[prizeIndex]}번 경품"

        val rotateAnim = RotateAnimation(
            0f, mSpinRevolution + end,
            Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )
        rotateAnim.interpolator = DecelerateInterpolator()
        rotateAnim.repeatCount = 0
        rotateAnim.duration = mSpinDuration
        rotateAnim.setAnimationListener(this)
        rotateAnim.fillAfter = true
        pointerImageView!!.startAnimation(rotateAnim)

    }

    override fun onAnimationStart(animation: Animation?) {
        infoText!!.text = "결과는...?"
    }

    override fun onAnimationEnd(animation: Animation?) {
        infoText!!.text = prizeText
    }

    override fun onAnimationRepeat(animation: Animation?) {
    }

    private inner class PowerTouchListener : View.OnTouchListener {
        override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {

            when (motionEvent!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    flag = true
                    count = 0
                    Thread {
                        while (flag) {
                            count++
                            if (count == 100) {
                                try {
                                    Thread.sleep(100)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                count = 0
                            }
                            try {
                                Thread.sleep(10)
                            } catch (e: InterruptedException) {
                                e.printStackTrace()
                            }
                        }
                    }.start()
                    return true
                }

                MotionEvent.ACTION_UP -> {
                    flag = false
                    startSpinner()
                    return false
                }

            }

            return false
        }

    }

    private fun editTextProcess() {

        binding.etPrize8.setOnEditorActionListener { textView, action, keyEvent ->
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

}