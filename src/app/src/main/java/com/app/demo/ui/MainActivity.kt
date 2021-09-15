package com.app.demo.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.demo.R
import com.app.demo.base.BaseActivity
import com.app.demo.databinding.ActivityMainBinding
import com.app.demo.utils.VerticalSpaceItemDecoration
import com.app.demo.utils.px
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adapter = ProductAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.addItemDecoration(VerticalSpaceItemDecoration(16.px))
        binding.recyclerView.adapter = adapter

        setupSubscriptions()
        setupListeners()
    }

    private fun setupSubscriptions() {

        viewModel.stateLiveData.observe { state ->

            state.noInternetConnectionError?.getContentIfNotHandled()?.let {
                showNoConnectionAlert()
            }

            state.noInternetConnectionError?.getContentIfNotHandled()?.let {
                showErrorDialog(getString(R.string.error), getString(R.string.error))
            }

            binding.loadingView.isVisible = state.isLoading
            binding.recyclerView.isVisible = !state.isLoading
            adapter.updateContent(state.products)
        }

    }

    private fun setupListeners() {
        binding.button.setOnClickListener {
            viewModel.getProducts()
        }
    }

}