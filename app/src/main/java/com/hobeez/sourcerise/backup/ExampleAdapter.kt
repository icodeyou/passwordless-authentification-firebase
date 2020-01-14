package com.hobeez.sourcerise.backup

import android.content.Context
import android.widget.TextView
import com.hobeez.sourcerise.R
import com.hobeez.sourcerise.ui.util.SuperAdapter

class ExampleAdapter(listString: ArrayList<String>, context: Context) :
    SuperAdapter<String>(listString, R.layout.backup_custom_view, null) {

    init {
        onBinding = { view, string ->
            view.findViewById<TextView>(R.id.textview_example).text = string
        }
    }
}
