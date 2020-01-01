package com.timesanimation.adapter

import com.timesanimation.R
import com.timesanimation.Utils.Utils.getDimensionFromDp
import com.timesanimation.Utils.Utils.getScreenHeight
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.recyclerview.widget.RecyclerView

open class RecyclerViewAdapter(var context: Activity) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        updateViewHolderHeight(holder, position)
    }

    private fun updateViewHolderHeight(holder: ViewHolder, position: Int) {
        val height = cardHeight
        val layoutParams = holder.cardView.layoutParams as RelativeLayout.LayoutParams
        layoutParams.height = height
        holder.itemView.layoutParams = layoutParams
        holder.cardView.cardElevation = getDimensionFromDp(4f, context)
        if (position == 0) {
            holder.guidelineStart.setGuidelinePercent(0.0f)
            holder.guidelineEnd.setGuidelinePercent(1.0f)
        }
    }

    private val cardHeight: Int
        private get() = getScreenHeight(context) * 65 / 100

    override fun getItemCount(): Int {
        return 10 //I should not do this
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView
        var constraintLayout: ConstraintLayout
        var guidelineStart: Guideline
        var guidelineEnd: Guideline

        init {
            cardView = itemView.findViewById(R.id.cardView)
            constraintLayout = itemView.findViewById(R.id.card_view_container)
            guidelineStart = constraintLayout.findViewById(R.id.guidelineStart)
            guidelineEnd = constraintLayout.findViewById(R.id.guidelineEnd)
        }
    }

}