package com.timesanimation.Utils

import com.timesanimation.R
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout

/**
 * Created by Yogesh Kumar.
 */
class CollapsingBehaviour : CoordinatorLayout.Behavior<AppCompatImageView> {
    private var hasTransitionValues = false
    private var factor = 0f
    /**
     * calculated from child view
     */
    private var startImageXCoord = 0
    private var startImageYCoord = 0
    private var startImageHeight = 0
    private var startToolbarHeight = 0
    /**
     * Collected from attributes provided by developer
     */
    private var finalToolbarHeight = 0f
    private var finalImageXCoord = 0f
    private var finalImageYCoord = 0f
    private var finalImageHeight = 0f
    /**
     * Delta of each transition value
     */
    private var amountOfImageSizeToReduce = 0f
    private var amountOfToolbarToMove = 0f
    private var amountToMoveXPosition = 0f
    private var amountToMoveYPosition = 0f

    constructor() {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let { fetchAttributes(context, it) }
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: AppCompatImageView, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: AppCompatImageView, dependency: View): Boolean {
        calculateTransitionValues(child, dependency)
        computeProgressAndMorphImage(child, dependency)
        return true
    }

    private fun computeProgressAndMorphImage(child: AppCompatImageView, dependency: View) {
        val progress = computeProgress(dependency)
        updateImageSize(child, progress)
        updateImagePosition(child, progress)
        updateImageAlpha(child, progress)
    }

    /**
     * update image alpha between 0 and 1
     *
     * @param child
     * @param progress
     */
    private fun updateImageAlpha(child: AppCompatImageView, progress: Float) { //float p = progress * 1.25f;
        child.alpha = (100 - progress) / 100
    }

    /**
     * calculate progress of movement of dependency
     *
     * @param dependency
     * @return
     */
    private fun computeProgress(dependency: View): Float {
        var currentToolbarHeight = startToolbarHeight + dependency.y
        // don't go below configured min height for calculations (it does go passed the toolbar)
        currentToolbarHeight = if (currentToolbarHeight < finalToolbarHeight) finalToolbarHeight else currentToolbarHeight
        val amountAlreadyMoved = startToolbarHeight - currentToolbarHeight
        // how much % of expand we reached
        return 100 * amountAlreadyMoved / amountOfToolbarToMove * factor
    }

    /**
     * update Image position
     *
     * @param child
     * @param progress
     */
    private fun updateImagePosition(child: AppCompatImageView, progress: Float) {
        val p = progress * 1.25f
        val distanceXToSubtract = progress * amountToMoveXPosition / 100
        val distanceYToSubtract = progress * amountToMoveYPosition / 100
        val newXPosition = startImageXCoord - distanceXToSubtract
        child.x = newXPosition
        child.y = startImageYCoord - distanceYToSubtract
        Log.d("yolo", "Progress - > $progress")
        Log.d("yolo", "Y to subtract - > $distanceYToSubtract")
    }

    /**
     * Update Image size
     *
     * @param child
     * @param progress
     */
    private fun updateImageSize(child: AppCompatImageView, progress: Float) {
        val p = progress * 1.25f
        val heightToSubtract = progress * amountOfImageSizeToReduce / 100
        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        lp.width = (startImageHeight - heightToSubtract).toInt()
        lp.height = (startImageHeight - heightToSubtract).toInt()
        child.layoutParams = lp
    }

    /**
     * make child change in relation to dependency (toolbar in this case) in both size and position,
     * init with properties from layout
     *
     * @param child
     * @param dependency
     */
    private fun calculateTransitionValues(
            child: AppCompatImageView,
            dependency: View) {
        if (!hasTransitionValues) { // form initial layout
            startImageHeight = child.height
            startImageXCoord = child.x.toInt()
            startImageYCoord = child.y.toInt()
            startToolbarHeight = dependency.height
            // some calculated fields
            amountOfToolbarToMove = startToolbarHeight - finalToolbarHeight
            amountOfImageSizeToReduce = startImageHeight - finalImageHeight
            amountToMoveXPosition = startImageXCoord - finalImageXCoord
            amountToMoveYPosition = startImageYCoord - finalImageYCoord
            hasTransitionValues = true
        }
    }

    private fun fetchAttributes(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CustomCollapseBehaviour)
        finalImageXCoord = a.getDimension(R.styleable.CustomCollapseBehaviour_finalX, 0f)
        finalImageYCoord = a.getDimension(R.styleable.CustomCollapseBehaviour_finalY, 0f)
        finalImageHeight = a.getDimension(R.styleable.CustomCollapseBehaviour_finalWidth, 0f)
        finalToolbarHeight = a.getDimension(R.styleable.CustomCollapseBehaviour_finalToolbarHeight, getDefaultHeight(context))
        factor = a.getFloat(R.styleable.CustomCollapseBehaviour_factor, 1f)
        a.recycle()
    }

    private fun getDefaultHeight(context: Context): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56f, context.resources.displayMetrics)
    }
}