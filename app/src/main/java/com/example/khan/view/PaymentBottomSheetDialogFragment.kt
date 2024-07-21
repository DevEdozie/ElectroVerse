package com.example.khan.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.khan.R
import com.example.khan.databinding.BottomModalLayoutBinding
import com.example.khan.viewmodel.MainActivityViewmodel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PaymentBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomModalLayoutBinding
    // Obtain ViewModel from the parent activity
    private val viewModel: MainActivityViewmodel by activityViewModels()

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
            findNavController().navigate(R.id.action_checkoutFragment_to_paymentSuccessfulFragment2)
            // Add cart items to orders, also empty the cart
            viewModel.checkout()
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

//        if (allInputsFilled) {
//            binding.proceedToPaymentBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
//        } else {
//            binding.proceedToPaymentBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.faded_blue))
//        }
        val buttonImage = if (allInputsFilled) {
            R.drawable.blue_button
        } else {
            R.drawable.faded_blue_button // Replace with your image resource for disabled state
        }
        binding.proceedToPaymentBtn.setImageResource(buttonImage)
    }
}