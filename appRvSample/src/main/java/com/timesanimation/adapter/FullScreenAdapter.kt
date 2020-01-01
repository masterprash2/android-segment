package com.timesanimation.adapter

import com.timesanimation.R
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Yogesh Kumar.
 */
class FullScreenAdapter(context: Activity) : RecyclerViewAdapter(context) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card_fill, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
    }
}