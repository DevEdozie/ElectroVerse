package com.example.khan.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.khan.R
import com.example.khan.databinding.BottomModalLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomModalLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomModalLayoutBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.proceedToPaymentBtn.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.fullNameEt.addTextChangedListener(textWatcher)
        binding.emailEt.addTextChangedListener(textWatcher)
        binding.phoneNumberEt.addTextChangedListener(textWatcher)

        binding.proceedToPaymentBtn.setOnClickListener {
            // Add navigation
            dismiss()
        }

        binding.cancelPaymentIc.setOnClickListener {
            dismiss()
        }


    }

    private fun validateInputs() {
        val name = binding.fullNameEt.text.toString().trim()
        val email = binding.emailEt.text.toString().trim()
        val phone = binding.phoneNumberEt.text.toString().trim()

        val allInputsFilled = name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()
        binding.proceedToPaymentBtn.isEnabled = allInputsFilled

        if (allInputsFilled) {
            binding.proceedToPaymentBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
        } else {
            binding.proceedToPaymentBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.faded_blue))
        }
    }
}