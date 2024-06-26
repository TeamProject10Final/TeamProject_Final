package com.nomorelateness.donotlate.feature.mypromise.presentation.view.dialog

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.nomorelateness.donotlate.R
import com.nomorelateness.donotlate.databinding.DialogRadiobuttonBinding
import com.nomorelateness.donotlate.feature.mypromise.presentation.model.Mode

class RadioButtonDialog(private val callback: (String) -> Unit) :
    DialogFragment() {

    private var _binding: DialogRadiobuttonBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        return super.onCreateView(inflater, container, savedInstanceState)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogRadiobuttonBinding.inflate(LayoutInflater.from(context))
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val modeArray = resources.getStringArray(R.array.modeArray)
            binding.rbTransit.text = modeArray[0]
            binding.rbDriving.text = modeArray[1]
            binding.rbWalking.text = modeArray[2]
            binding.rbBicycling.text = modeArray[3]

            binding.btnRadioConfirm.setOnClickListener {
                val selectedId = binding.radioGroup.checkedRadioButtonId
                val selectedKey = when (selectedId) {
                    R.id.rb_Transit -> Mode.OPTION_1.key
                    R.id.rb_Driving -> Mode.OPTION_2.key
                    R.id.rb_Walking -> Mode.OPTION_3.key
                    R.id.rb_Bicycling -> Mode.OPTION_4.key
                    else -> null
                }
                selectedKey?.let { key ->
                    callback(key)
                    dismiss()
                }
            }
            binding.btnRadioCancel.setOnClickListener {
                dismiss()
            }


            builder.setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}