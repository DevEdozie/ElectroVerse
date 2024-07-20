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
import com.example.khan.adapter.WishListAdapter
import com.example.khan.databinding.FragmentWishListBinding
import com.example.khan.local_db.entity.WishListItem
import com.example.khan.viewmodel.MainActivityViewmodel


class WishListFragment : Fragment() {

    private lateinit var binding: FragmentWishListBinding

    // Obtain ViewModel from the parent activity
    private val viewModel: MainActivityViewmodel by activityViewModels()

    // Declare the WishListItemsAdapter and binding object
    private lateinit var wishListAdapter: WishListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishListBinding.inflate(layoutInflater, container, false)
        setUpDiscoverProducts()
        setUpRecyclerView()
        setUpBackArrowNavigation()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setUpDiscoverProducts() {
        binding.discoverProductsBtn.setOnClickListener {
            findNavController().popBackStack(R.id.productsScreenFragment, false)
        }
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
        wishListAdapter = WishListAdapter(viewModel)

        binding.wishListRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = wishListAdapter
        }


        // Observe items data from the ViewModel and submit to the adapter
        viewModel.getAllWishListItems().observe(viewLifecycleOwner) { items ->
            wishListAdapter.differ.submitList(items)
            updateUI(items)
        }
    }

    private fun updateUI(wishListItems: List<WishListItem>) {
        if (wishListItems.isEmpty()) {
            binding.wishlistPlaceholderIc.visibility = View.VISIBLE
            binding.wishListTv.visibility = View.VISIBLE
            binding.discoverProductsBtn.visibility = View.VISIBLE
            binding.wishListRecyclerview.visibility = View.GONE
        } else {
            binding.wishListRecyclerview.visibility = View.VISIBLE
            binding.wishlistPlaceholderIc.visibility = View.GONE
            binding.wishListTv.visibility = View.GONE
            binding.discoverProductsBtn.visibility = View.GONE
        }
    }
}