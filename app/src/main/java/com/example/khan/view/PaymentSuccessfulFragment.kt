package com.example.khan.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.khan.R
import com.example.khan.databinding.FragmentPaymentSuccessfulBinding


class PaymentSuccessfulFragment : Fragment() {

    private lateinit var binding: FragmentPaymentSuccessfulBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPaymentSuccessfulBinding.inflate(layoutInflater, container, false)
        setUpOkayButton()
        setUpBackButton()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setUpBackButton() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Pop the back stack to return to the previous fragment
                    findNavController().popBackStack(R.id.productsScreenFragment, false)
                }
            })
    }

    private fun setUpOkayButton() {
        binding.okBtn.setOnClickListener {
            findNavController().popBackStack(R.id.productsScreenFragment, false)
        }

    }


}