package com.app.demo.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.app.demo.utils.Event

class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, { onChanged(it) })
    }

    fun <T> LiveData<Event<T>>.observeNotHandled(onChanged: (T) -> Unit) {
        observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandled()?.let {
                onChanged(it)
            }
        })
    }
}