package in.til.yogeshkumar.timesanimation.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.transition.TransitionManager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import in.til.yogeshkumar.timesanimation.R;

/**
 * Created by Yogesh Kumar.
 */
public class CollapsingBehaviour extends CoordinatorLayout.Behavior<AppCompatImageView> {

    private boolean hasTransitionValues = false;
    private float factor;

    /**
     * calculated from child view
     */
    private int startImageXCoord;
    private int startImageYCoord;
    private int startImageHeight;
    private int startToolbarHeight;


    /**
     * Collected from attributes provided by developer
     */
    private float finalToolbarHeight;
    private float finalImageXCoord;
    private float finalImageYCoord;
    private float finalImageHeight;

    /**
     * Delta of each transition value
     */
    private float amountOfImageSizeToReduce;
    private float amountOfToolbarToMove;
    private float amountToMoveXPosition;
    private float amountToMoveYPosition;

    public CollapsingBehaviour() {

    }

    public CollapsingBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            fetchAttributes(context, attrs);
        }
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, AppCompatImageView child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, AppCompatImageView child, View dependency) {
        calculateTransitionValues(child, dependency);
        computeProgressAndMorphImage(child, dependency);
        return true;
    }

    private void computeProgressAndMorphImage(AppCompatImageView child, View dependency) {
        final float progress = computeProgress(dependency);

        updateImageSize(child, progress);

        updateImagePosition(child, progress);

        updateImageAlpha(child, progress);
    }

    /**
     * update image alpha between 0 and 1
     *
     * @param child
     * @param progress
     */
    private void updateImageAlpha(AppCompatImageView child, float progress) {
        //float p = progress * 1.25f;
        child.setAlpha((100 - progress)/100);
    }

    /**
     * calculate progress of movement of dependency
     *
     * @param dependency
     * @return
     */
    private float computeProgress(View dependency) {
        float currentToolbarHeight = startToolbarHeight + dependency.getY();

        // don't go below configured min height for calculations (it does go passed the toolbar)
        currentToolbarHeight = currentToolbarHeight < finalToolbarHeight ? finalToolbarHeight : currentToolbarHeight;
        final float amountAlreadyMoved = startToolbarHeight - currentToolbarHeight;

        // how much % of expand we reached
        return (100 * amountAlreadyMoved / amountOfToolbarToMove) * factor;
    }

    /**
     * update Image position
     *
     * @param child
     * @param progress
     */
    private void updateImagePosition(AppCompatImageView child, float progress) {
        float p = progress * 1.25f;
        final float distanceXToSubtract = progress * amountToMoveXPosition / 100;
        final float distanceYToSubtract = progress * amountToMoveYPosition / 100;
        float newXPosition = startImageXCoord - distanceXToSubtract;
        child.setX(newXPosition);
        child.setY(startImageYCoord - distanceYToSubtract);
        Log.d("yolo", "Progress - > " + String.valueOf(progress));
        Log.d("yolo", "Y to subtract - > " + String.valueOf(distanceYToSubtract));
    }

    /**
     * Update Image size
     *
     * @param child
     * @param progress
     */
    private void updateImageSize(AppCompatImageView child, float progress) {
        float p = progress * 1.25f;
        final float heightToSubtract = progress * amountOfImageSizeToReduce / 100;
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        lp.width = (int) (startImageHeight - heightToSubtract);
        lp.height = (int) (startImageHeight - heightToSubtract);
        child.setLayoutParams(lp);
    }

    /**
     * make child change in relation to dependency (toolbar in this case) in both size and position,
     * init with properties from layout
     *
     * @param child
     * @param dependency
     */
    private void calculateTransitionValues(
            final AppCompatImageView child,
            final View dependency) {

        if (!hasTransitionValues) {
            // form initial layout
            startImageHeight = child.getHeight();
            startImageXCoord = (int) child.getX();
            startImageYCoord = (int) child.getY();
            startToolbarHeight = dependency.getHeight();
            // some calculated fields
            amountOfToolbarToMove = startToolbarHeight - finalToolbarHeight;
            amountOfImageSizeToReduce = startImageHeight - finalImageHeight;
            amountToMoveXPosition = startImageXCoord - finalImageXCoord;
            amountToMoveYPosition = startImageYCoord - finalImageYCoord;
            hasTransitionValues = true;
        }
    }

    private void fetchAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCollapseBehaviour);
        finalImageXCoord = a.getDimension(R.styleable.CustomCollapseBehaviour_finalX, 0);
        finalImageYCoord = a.getDimension(R.styleable.CustomCollapseBehaviour_finalY, 0);
        finalImageHeight = a.getDimension(R.styleable.CustomCollapseBehaviour_finalWidth, 0);
        finalToolbarHeight = a.getDimension(R.styleable.CustomCollapseBehaviour_finalToolbarHeight, getDefaultHeight(context));
        factor = a.getFloat(R.styleable.CustomCollapseBehaviour_factor, 1);
        a.recycle();
    }

    private float getDefaultHeight(Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, context.getResources().getDisplayMetrics());
    }
}
