package com.hobeez.sourcerise.ui.util

import android.content.Context
import android.text.SpannableString
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hobeez.sourcerise.R

/**
 * The SmartRecyclerViewLayout operates in relation with the SmartRecyclerView.
 * It contains a SmartRecyclerView and a TextView to display the error message or the empty message.
 */
class SmartRecyclerViewLayout(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {

    private val emptyView: TextView
    private val smartRecyclerView: SmartRecyclerView

    init {
        ConstraintLayout.inflate(context, R.layout.custom_rv, this)
        smartRecyclerView = findViewById(R.id.smartRecyclerView)
        emptyView = findViewById(R.id.emptyMessage)
        context.theme.obtainStyledAttributes(
            attributeSet,
            R.styleable.SmartRecyclerViewLayout,
            0, 0
        ).apply {
            try {
                setEmptyMessage(getString(R.styleable.SmartRecyclerViewLayout_emptyViewText) ?: "")

                val globalPadding = getDimension(R.styleable.SmartRecyclerViewLayout_paddingRV, 0f).toInt()
                val myPaddingLeft = getDimension(R.styleable.SmartRecyclerViewLayout_paddingLeftRV, 0f).toInt()
                val myPaddingTop = getDimension(R.styleable.SmartRecyclerViewLayout_paddingTopRV, 0f).toInt()
                val myPaddingRight = getDimension(R.styleable.SmartRecyclerViewLayout_paddingRightRV, 0f).toInt()
                val myPaddingBottom = getDimension(R.styleable.SmartRecyclerViewLayout_paddingBottomRV, 0f).toInt()
                findViewById<SmartRecyclerView>(R.id.smartRecyclerView).setPadding(
                    globalPadding + myPaddingLeft,
                    globalPadding + myPaddingTop,
                    globalPadding + myPaddingRight,
                    globalPadding + myPaddingBottom
                )
            } finally {
                recycle()
            }
        }
        setEmptyMessage(context.getString(R.string.default_message_empty_view_rv))
    }

    /**
     * Set a message your recyclerView will display as soon as your list is (or become) empty.
     * @param message the text you want to display to the user.
     */
    fun setEmptyMessage(message: String) {
        smartRecyclerView.setEmptyView(emptyView, message)
    }

    fun setEmptyMessage(message: SpannableString) {
        smartRecyclerView.setEmptyView(emptyView, message)
    }

    /**
     * Display an error message to the user instead of your data list. When this method is called
     * the message is instantly displayed to the user.
     * @param message the text you want to display to the user.
     */
    fun displayErrorMessage(message: String? = "") {
        smartRecyclerView.displayError(message)
    }
}
