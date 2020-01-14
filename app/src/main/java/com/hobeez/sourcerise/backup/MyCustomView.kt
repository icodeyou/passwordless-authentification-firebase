package com.hobeez.sourcerise.backup

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.hobeez.sourcerise.R

class MyCustomView(context: Context, attrs: AttributeSet?, scoreValue: Int) : RelativeLayout(context, attrs) {

    private val textView: TextView

    init {
        ConstraintLayout.inflate(context, R.layout.backup_custom_view, this)
        textView = this.findViewById(R.id.textview_example)
    }
}
