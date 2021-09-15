package com.app.demo.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.demo.R
import com.app.demo.databinding.ViewProductItemBinding
import com.app.demo.model.ProductItem

class ProductAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<ProductItem> = listOf()

    fun updateContent(newList: List<ProductItem>) {
        val diffCallback = ProductDiffCallback(this.list, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parentGroup: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parentGroup.context).inflate(R.layout.view_product_item, parentGroup, false)
        val binding = ViewProductItemBinding.bind(view)
        return ProductHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item: ProductItem = list[position]
        when (holder) {
            is ProductHolder -> item.apply { holder.bindItems(this) }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.view_product_item
    }

    inner class ProductHolder(private val binding: ViewProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bindItems(productItem: ProductItem) = with(itemView) {
            itemView.tag = productItem
            binding.nameLabel.text = productItem.title
            binding.descriptionLabel.text = productItem.description
        }
    }

}