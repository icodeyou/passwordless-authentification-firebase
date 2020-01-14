package com.hobeez.sourcerise.ui.util

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hobeez.sourcerise.R

/**
 * Custom RecyclerView - Used in SmartRecyclerViewLayout
 *
 * Thanks to the methods [setEmptyView] and [displayError],
 * this class can be used to display a message if the items list is empty,
 * and to display a message error if an error has been thrown.
 *
 * It is mainly used in SmartRecyclerViewLayout, where empty views are inflated.
 * Therefore, this class shouldn't have to be called.
 */
class SmartRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private var emptyView: TextView? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            displayViewOrEmptyMessage()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            displayViewOrEmptyMessage()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            displayViewOrEmptyMessage()
        }
    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)
        displayViewOrEmptyMessage()
    }

    internal fun displayViewOrEmptyMessage() {
        val isDataListEmpty = adapter?.itemCount == 0
        emptyView?.visibility =
            if (isDataListEmpty) View.VISIBLE
            else View.GONE
        visibility =
            if (isDataListEmpty) View.GONE
            else View.VISIBLE
    }

    fun setEmptyView(view: TextView, text: String) {
        view.text = text
        emptyView = view
    }

    fun setEmptyView(view: TextView, text: SpannableString) {
        view.text = text
        emptyView = view
    }

    fun displayError(errorMessage: String?) {
        emptyView?.text =
            if (!errorMessage.isNullOrEmpty()) errorMessage
            else context?.getString(R.string.error_default_message)
        emptyView?.visibility = View.VISIBLE
    }
}
