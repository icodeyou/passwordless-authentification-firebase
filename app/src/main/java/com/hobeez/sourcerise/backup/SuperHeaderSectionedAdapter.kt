package com.hobeez.sourcerise.backup

// *************************************************
// BASED ON THE LIBRARY SectionedRecyclerViewAdapter
// *************************************************

// import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter

/**
 * SuperHeaderSectionedAdapter must be used with for lists with sections and header
 * If you don't need any sections, use SuperAdapter or SuperAdapterWithHeader instead.
 */

// Following constants must be different from the ones in SectionedRecyclerViewAdapter
private const val SECTION_TYPE_HEADER = 99
private const val SECTION_TYPE_ITEM = 100

/*
open class SuperHeaderSectionedAdapter : SectionedRecyclerViewAdapter {

    private var view: View?
    private var headerLayoutId: Int

    */
/**
     * Constructor using a view
     * @param headerView view created dynamically
     *//*

    constructor(headerView: View) : super() {
        this.view = headerView
        this.headerLayoutId = 0
    }

    */
/**
     * Constructor using a layout ID
     * @param headerLayoutId Layout ID (R.layout.layout_example)
     *//*

    constructor(headerLayoutId: Int) : super() {
        this.view = null
        this.headerLayoutId = headerLayoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == SECTION_TYPE_HEADER) {
            return HeaderViewHolder(view ?: parent.inflate(headerLayoutId))
        } else {
            return super.onCreateViewHolder(parent, viewType)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return SECTION_TYPE_HEADER
        } else {
            return super.getItemViewType(position - 1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is HeaderViewHolder) {
            return super.onBindViewHolder(holder, position - 1)
        }
    }

    class HeaderViewHolder(parent: View) : RecyclerView.ViewHolder(parent)

    class ItemViewHolder<T>(parent: View, private val onBinding: (view: View, item: T) -> Unit) :
        RecyclerView.ViewHolder(parent) {

        fun bindView(item: T) {
            onBinding.invoke(itemView, item)
        }
    }
}
*/
