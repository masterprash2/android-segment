package com.timesanimation.Utils

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by Yogesh Kumar.
 */
class ScaleInBottomAnimator : RecyclerView.ItemAnimator() {
    var mPendingAdd: MutableList<RecyclerView.ViewHolder> = ArrayList()
    var mPendingRemove: MutableList<RecyclerView.ViewHolder> = ArrayList()
    override fun animateDisappearance(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo?): Boolean {
        mPendingRemove.add(viewHolder)
        return false
    }

    override fun animateAppearance(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo?, postLayoutInfo: ItemHolderInfo): Boolean {
        viewHolder.itemView.alpha = 0.0f
        return mPendingAdd.add(viewHolder)
    }

    override fun animatePersistence(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo): Boolean {
        return false
    }

    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo): Boolean {
        return false
    }

    override fun runPendingAnimations() {
        val animationDuration = 300
        if (!mPendingAdd.isEmpty()) {
            for (viewHolder in mPendingAdd) {
                val target = viewHolder.itemView
                target.pivotX = target.measuredWidth / 2.toFloat()
                target.pivotY = target.measuredHeight / 2.toFloat()
                val animator = AnimatorSet()
                animator.playTogether(
                        ObjectAnimator.ofFloat(target, "translationX", -target.measuredWidth.toFloat(), 0.0f),
                        ObjectAnimator.ofFloat(target, "alpha", target.alpha, 1.0f)
                )
                animator.setTarget(target)
                animator.duration = animationDuration.toLong()
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.startDelay = animationDuration * viewHolder.position / 10.toLong()
                animator.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}
                    override fun onAnimationEnd(animation: Animator) {
                        mPendingAdd.remove(viewHolder)
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                animator.start()
            }
        }
        if (!mPendingRemove.isEmpty()) {
            for (viewHolder in mPendingRemove) {
                val target = viewHolder.itemView
                target.pivotX = target.measuredWidth / 2.toFloat()
                target.pivotY = target.measuredHeight / 2.toFloat()
                val animator = AnimatorSet()
                animator.playTogether(
                        ObjectAnimator.ofFloat(target, "translationX", 0.0f, target.measuredWidth.toFloat()),
                        ObjectAnimator.ofFloat(target, "alpha", target.alpha, 0.0f)
                )
                animator.setTarget(target)
                animator.duration = animationDuration.toLong()
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.startDelay = animationDuration * viewHolder.position / 10.toLong()
                animator.start()
            }
        }
    }

    override fun endAnimation(viewHolder: RecyclerView.ViewHolder) {}
    override fun endAnimations() {}
    override fun isRunning(): Boolean {
        return !mPendingAdd.isEmpty() || !mPendingRemove.isEmpty()
    }
}