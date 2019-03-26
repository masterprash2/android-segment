package in.til.yogeshkumar.timesanimation.adapter.segment;

import android.Manifest;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clumob.segment.manager.SegmentViewHolder;

import in.til.yogeshkumar.timesanimation.R;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public class SampleSegmentView extends SegmentViewHolder {

    public SampleSegmentView(Context context, LayoutInflater layoutInflater, ViewGroup parentView) {
        super(context, layoutInflater, parentView);
    }

    @Override
    protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.item_card_fill, viewGroup, false);
    }

    @Override
    protected void onBind() {
    }

    @Override
    protected void onUnBind() {

    }
}
