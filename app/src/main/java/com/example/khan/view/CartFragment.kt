package com.example.khan.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khan.R
import com.example.khan.adapter.CartItemsAdapter
import com.example.khan.adapter.FeaturedProductsAdapter
import com.example.khan.adapter.ProductAdapter
import com.example.khan.databinding.FragmentCartBinding
import com.example.khan.databinding.FragmentProductsScreenBinding
import com.example.khan.local_db.entity.CartItem
import com.example.khan.local_db.entity.OrderItem
import com.example.khan.viewmodel.MainActivityViewmodel


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding

    // Obtain ViewModel from the parent activity
    private val viewModel: MainActivityViewmodel by activityViewModels()

    // Declare the CartItemsAdapter and binding object
    private lateinit var cartItemsAdapter: CartItemsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        setUpRecyclerView()
        setUpBackArrowNavigation()
        setUpCheckoutNavigation()
        observePriceSummary() // Observe price summary LiveData
        // Inflate the layout for this fragment
        return binding.root
    }

    // Function to set up back arrow navigation
    private fun setUpBackArrowNavigation() {
        // Set listener for back arrow icon
        binding.backArrow.setOnClickListener {
            // Pop back stack to ProductsScreenFragment
            findNavController().popBackStack(R.id.productsScreenFragment, false)
        }
    }


    // Function to set up the RecyclerView with the ProductAdapter
    private fun setUpRecyclerView() {
        cartItemsAdapter = CartItemsAdapter(viewModel)

        binding.cartRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartItemsAdapter
        }


        // Observe items data from the ViewModel and submit to the adapter
        viewModel.getAllCartItems().observe(viewLifecycleOwner) { items ->
            cartItemsAdapter.differ.submitList(items)
            updateUI(items)
        }
    }

    private fun setUpCheckoutNavigation() {
        binding.checkoutBtn.setOnClickListener {
            findNavController().navigate(R.id.action_cartFragment2_to_checkoutFragment)
        }
    }

    private fun updateUI(cartItems: List<CartItem>) {
        if (cartItems.isEmpty()) {
            binding.cartPlaceholder.visibility = View.VISIBLE
            binding.totalPrice.visibility = View.GONE
            binding.totalPriceTv.visibility = View.GONE
            binding.cartRecyclerview.visibility = View.GONE
            binding.checkoutBtn.visibility = View.GONE
        } else {
            binding.cartRecyclerview.visibility = View.VISIBLE
            binding.totalPrice.visibility = View.VISIBLE
            binding.totalPriceTv.visibility = View.VISIBLE
            binding.checkoutBtn.visibility = View.VISIBLE
            binding.cartPlaceholder.visibility = View.GONE


        }
    }

    // Function to observe price summary LiveData
    private fun observePriceSummary() {
        viewModel.totalPrice.observe(viewLifecycleOwner) { totalPrice ->
            binding.totalPrice.text = "$$totalPrice"
        }
    }


}