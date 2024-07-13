package com.example.khan.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.khan.R
import com.example.khan.adapter.FeaturedProductsAdapter
import com.example.khan.adapter.ProductAdapter
import com.example.khan.databinding.FragmentProductDetailBinding
import com.example.khan.model.BaseResponse
import com.example.khan.model.Item
import com.example.khan.viewmodel.MainActivityViewmodel

class ProductDetailFragment : Fragment() {

    // Declare the binding object for this fragment
    private lateinit var binding: FragmentProductDetailBinding

    // Declare the FeaturedProductAdapter and binding object
    private lateinit var featuredProductAdapter: FeaturedProductsAdapter

    // Obtain the ViewModel from the parent activity
    private val viewModel: MainActivityViewmodel by activityViewModels()


    private var selectedColor: Int? = null
    private var selectedColorView: View? = null

    private var selectedNumber: Int? = null
    private var selectedTextView: TextView? = null

    // Inflate the fragment layout and initialize necessary components
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProductDetailBinding.inflate(layoutInflater, container, false)

        // Set up back arrow navigation
        setUpBackArrowNavigation()

        // Observe product data from the ViewModel
        observeProductData()

        // Set up List
        setUpRecyclerView()
        //
        setupOptions(
            container = binding.colorOptionsLayout,
            options = listOf(Color.RED, Color.BLUE, Color.BLACK),
            isColor = true
        )

        setupOptions(
            container = binding.numberOptionsLayout,
            options = (32..36).toList(),
            isColor = false
        )


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

    // Function to set up product details in the UI
    private fun setUpProductDetail(product: Item?) {
        // Set product title text
        binding.productTitle.text = product?.name

        // Set product description text
        binding.productDesc.text = "${product?.description}"

        // Check if photos list is not empty and load the first image
        val imageUrl = "https://api.timbu.cloud/images/${product?.photos?.get(0)?.url}"
        if (imageUrl.isNotEmpty()) {
            // Display image from URL
            Glide.with(requireContext()).load(imageUrl).into(binding.productImage)
        } else {
            // Set a placeholder image or handle the case where there is no image
            binding.productImage.setImageResource(R.drawable.terrex_free_hiker)
        }
    }

    // Function to observe product data from the ViewModel
    private fun observeProductData() {
        viewModel.timbuProduct.observe(viewLifecycleOwner) { timbuProduct ->
            when (timbuProduct) {
                is BaseResponse.Loading -> {
                    // Show loading toast
                    Toast.makeText(
                        requireContext(),
                        "Loading....",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is BaseResponse.Success -> {
                    // On success, set up product details
                    val product = timbuProduct.data
                    setUpProductDetail(product)
                }

                is BaseResponse.Error -> {
                    // On error, show error message
                    val errorMessage = timbuProduct.message
                    Toast.makeText(
                        requireContext(),
                        "${errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setupOptions(container: LinearLayout, options: List<Any>, isColor: Boolean) {
        for (option in options) {
            if (isColor) {
                val colorView = View(context).apply {
                    setBackgroundColor(option as Int)
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1f
                    ).apply {
                        setMargins(8, 8, 8, 8)
                    }
                }

                colorView.setOnClickListener {
                    selectOption(option, colorView, true)
                }

                container.addView(colorView)
            } else {
                val number = option as Int
                val numberTextView = TextView(context).apply {
                    text = number.toString()
                    textSize = 16f
                    setPadding(8, 8, 8, 8)
                    setBackgroundResource(R.drawable.number_background)
                    setTextColor(Color.BLACK)
                    gravity = android.view.Gravity.START
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(8, 0, 8, 0)
                    }
                }

                numberTextView.setOnClickListener {
                    selectOption(number, numberTextView, false)
                }

                container.addView(numberTextView)
            }
        }
    }

    private fun selectOption(option: Any, view: View, isColor: Boolean) {
        if (isColor) {
            selectedColorView?.setBackgroundColor(selectedColorView?.tag as Int)

            view.setBackgroundColor(Color.parseColor("#007BFF"))
            selectedColor = option as Int
            selectedColorView = view

            Toast.makeText(
                context,
                "Selected color: #${Integer.toHexString(option)}",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            selectedTextView?.setBackgroundResource(R.drawable.number_background)

            view.setBackgroundResource(R.drawable.selected_number_background)
            selectedNumber = option as Int
            selectedTextView = view as TextView

            Toast.makeText(context, "Selected number: $option", Toast.LENGTH_SHORT).show()
        }
    }


    // Function to set up the RecyclerView with the ProductAdapter
    private fun setUpRecyclerView() {
        featuredProductAdapter = FeaturedProductsAdapter(viewModel)


        binding.productsRecyclerview.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = featuredProductAdapter
        }

        // Observe items data from the ViewModel and submit to the adapter
        viewModel.items.observe(viewLifecycleOwner) { items ->
//            productAdapter.differ.submitList(items)
            // Submit the filtered list to the adapter
            featuredProductAdapter.submitList((items))
        }
    }


}




