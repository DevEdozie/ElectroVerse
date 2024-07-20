package com.example.khan.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khan.R
import com.example.khan.adapter.CartItemsAdapter
import com.example.khan.adapter.CheckoutItemsAdapter
import com.example.khan.databinding.FragmentCartBinding
import com.example.khan.databinding.FragmentCheckoutBinding
import com.example.khan.viewmodel.MainActivityViewmodel


class CheckoutFragment : Fragment() {


    private lateinit var binding: FragmentCheckoutBinding

    // Obtain ViewModel from the parent activity
    private val viewModel: MainActivityViewmodel by activityViewModels()

    // Declare the CartItemsAdapter and binding object
    private lateinit var checkoutItemsAdapter: CheckoutItemsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCheckoutBinding.inflate(layoutInflater, container, false)
        setUpRecyclerView()
        setUpBackArrowNavigation()
        setUpProceedButton()
        return binding.root
    }

    // Function to set up back arrow navigation
    private fun setUpBackArrowNavigation() {
        // Set listener for back arrow icon
        binding.backArrow.setOnClickListener {
            // Pop back stack to ProductsScreenFragment
            findNavController().popBackStack(R.id.cartFragment2, false)
        }
    }


    // Function to set up the RecyclerView with the ProductAdapter
    private fun setUpRecyclerView() {
        checkoutItemsAdapter = CheckoutItemsAdapter(viewModel)

        binding.cartRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = checkoutItemsAdapter
        }


        // Observe items data from the ViewModel and submit to the adapter
        viewModel.getAllCartItems().observe(viewLifecycleOwner) { items ->
            checkoutItemsAdapter.differ.submitList(items)
        }
    }

    // Function to show bottom modal on "Proceed" button click
    private fun setUpProceedButton() {
        binding.proceedBtn.setOnClickListener {
            val bottomSheet = PaymentBottomSheetDialogFragment()
            bottomSheet.show(parentFragmentManager, "PaymentBottomSheetDialog")
        }
    }


}