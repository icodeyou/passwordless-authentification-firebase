package com.hobeez.sourcerise.backup

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import com.hobeez.sourcerise.R

class MyCustomButton : Button {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    init {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.backup_custom_button, null)
    }
}
