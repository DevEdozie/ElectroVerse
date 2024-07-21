package com.example.khan.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khan.R
import com.example.khan.adapter.CartItemsAdapter
import com.example.khan.adapter.CheckoutItemsAdapter
import com.example.khan.adapter.OrdersAdapter
import com.example.khan.databinding.FragmentCartBinding
import com.example.khan.databinding.FragmentOrdersBinding
import com.example.khan.local_db.entity.OrderItem
import com.example.khan.local_db.entity.WishListItem
import com.example.khan.viewmodel.MainActivityViewmodel


class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding

    // Obtain ViewModel from the parent activity
    private val viewModel: MainActivityViewmodel by activityViewModels()

    // Declare the Adapter
    private lateinit var ordersAdapter: OrdersAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        setUpRecyclerView()
        // Inflate the layout for this fragment
        return binding.root
    }

    // Function to set up the RecyclerView with the ProductAdapter
    private fun setUpRecyclerView() {
        ordersAdapter = OrdersAdapter(viewModel)

        binding.ordersRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ordersAdapter
        }


        // Observe items data from the ViewModel and submit to the adapter
        viewModel.getAllOrderedItems().observe(viewLifecycleOwner) { items ->
            ordersAdapter.differ.submitList(items)
            updateUI(items)
        }
    }


    private fun updateUI(orderItems: List<OrderItem>) {
        if (orderItems.isEmpty()) {
            binding.orderPlaceholderIc.visibility = View.VISIBLE
            binding.orderTv.visibility = View.VISIBLE
            binding.ordersRecyclerview.visibility = View.GONE
        } else {
            binding.ordersRecyclerview.visibility = View.VISIBLE
            binding.orderPlaceholderIc.visibility = View.GONE
            binding.orderTv.visibility = View.GONE

        }
    }


}