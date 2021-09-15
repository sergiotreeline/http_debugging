package com.app.demo.ui

import androidx.recyclerview.widget.DiffUtil
import com.app.demo.model.ProductItem

class ProductDiffCallback(
    private val oldList: List<ProductItem>,
    private val newList: List<ProductItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}