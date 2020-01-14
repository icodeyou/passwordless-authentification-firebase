package com.hobeez.sourcerise.ui.util

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hobeez.sourcerise.util.extensions.inflate

private const val TYPE_HEADER = 0
private const val TYPE_ITEM = 1
private const val TYPE_FOOTER = 2

/**
 * All adapters can now extend SuperAdapter. It helps building adapters quicker and evenly.
 * The new adapter must at least set the variables mData (with the type you wish) and onBinding.
 *
 * If you desire an adapter with header, use the secondary constructors
 */
open class SuperAdapter<T>(
    private var mData: ArrayList<T>,
    private var itemLayoutId: Int,
    private val onClickListener: ((T) -> Unit)? = null
) : RecyclerView.Adapter<SuperAdapter.SuperViewHolder<T>>() {

    /**
     * Constructor using a view for header and footer
     * @param data list of items in BaseAdapter
     * @param itemLayoutId layout of one item in BaseAdapter
     * @param headerView optional view created dynamically
     * @param footerView optional view created dynamically
     */
    constructor(
        data: ArrayList<T>,
        itemLayoutId: Int,
        onClickListener: ((T) -> Unit)? = null,
        headerView: View,
        footerView: View
    ) : this(data, itemLayoutId, onClickListener) {
        this.headerView = headerView
        this.footerView = footerView
        this.hasHeader = true
        this.hasFooter = true
    }

    /**
     * Constructor using a layout ID for header and footer
     * @param data list of items in BaseAdapter
     * @param itemLayoutId layout of one item in BaseAdapter
     * @param headerLayoutId optional layout ID (R.layout.header_layout_example)
     * @param footerLayoutId optional layout ID (R.layout.footer_layout_example)
     */
    constructor(
        data: ArrayList<T>,
        itemLayoutId: Int,
        onClickListener: ((T) -> Unit)? = null,
        headerLayoutId: Int,
        footerLayoutId: Int
    ) : this(data, itemLayoutId, onClickListener) {
        this.headerLayoutId = headerLayoutId
        this.footerLayoutId = footerLayoutId
        this.hasHeader = true
        this.hasFooter = true
    }

    private var headerView: View? = null
    private var headerLayoutId: Int? = null
    private var footerView: View? = null
    private var footerLayoutId: Int? = null
    private var hasHeader = false
    private var hasFooter = false
    protected var onBinding: (view: View, item: T) -> Unit = { _, _ -> Unit }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperAdapter.SuperViewHolder<T> {
        return when (viewType) {
            TYPE_HEADER -> {
                HeaderViewHolder(headerView ?: parent.inflate(headerLayoutId!!))
            }
            TYPE_FOOTER -> FooterViewHolder(footerView ?: parent.inflate(footerLayoutId!!))
            else -> ItemViewHolder(parent.inflate(itemLayoutId), onBinding)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasHeader) {
            getViewTypeWithHeader(position)
        } else if (hasFooter) {
            getViewTypeWithFooterOnly(position)
        } else {
            TYPE_ITEM
        }
    }

    private fun getViewTypeWithHeader(position: Int): Int {
        return when (position) {
            0 -> TYPE_HEADER
            mData.size + 1 -> if (hasFooter) TYPE_FOOTER else TYPE_ITEM
            else -> TYPE_ITEM
        }
    }

    private fun getViewTypeWithFooterOnly(position: Int): Int {
        return when (position) {
            mData.size -> TYPE_FOOTER
            else -> TYPE_ITEM
        }
    }

    override fun getItemCount(): Int {
        return mData.size + getExtraCell()
    }

    private fun getExtraCell(): Int {
        var extraCell = 0
        if (hasHeader) {
            extraCell++
        }
        if (hasFooter) {
            extraCell++
        }
        return extraCell
    }

    override fun onBindViewHolder(holder: SuperViewHolder<T>, position: Int) {
        // Correct the position if we have a header
        if (holder is ItemViewHolder) {
            if (hasHeader)
                holder.bindView(mData[position - 1], onClickListener)
            else
                holder.bindView(mData[position], onClickListener)
        }
    }

    fun addData(newItem: T) {
        mData.add(newItem)
        notifyDataSetChanged()
    }

    fun setData(items: ArrayList<T>) {
        mData = items
        notifyDataSetChanged()
    }

    open class SuperViewHolder<T>(parent: View) : RecyclerView.ViewHolder(parent)

    class HeaderViewHolder<T>(parent: View) : SuperViewHolder<T>(parent)

    class FooterViewHolder<T>(parent: View) : SuperViewHolder<T>(parent)

    class ItemViewHolder<T>(parent: View, private val onBinding: (view: View, item: T) -> Unit) :
        SuperViewHolder<T>(parent) {

        fun bindView(item: T, onClickListener: ((T) -> Unit)?) {
            onBinding.invoke(itemView, item)
            itemView.setOnClickListener { onClickListener?.invoke(item) }
        }
    }
}
