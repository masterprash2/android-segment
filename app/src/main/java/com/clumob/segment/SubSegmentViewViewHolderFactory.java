package com.clumob.segment;

import android.content.Context;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.manager.SegmentViewHolderFactory;
import com.clumob.segment.manager.SegmentViewHolder;

import java.util.Random;

/**
 * Created by prashant.rathore on 08/07/18.
 */

class SubSegmentViewViewHolderFactory implements SegmentViewHolderFactory {
    @Override
    public SegmentViewHolder<?, ?> create(final Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
        return new SegmentViewHolder<Object, SegmentController<Object>>(context,layoutInflater,parentView) {



            @Override
            protected View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
                return layoutInflater.inflate(R.layout.segment_sub_item,viewGroup,false);
            }

            @Override
            protected void onBind() {
                TextView viewById = getView().findViewById(R.id.subText);
                viewById.setText(String.valueOf(new Random().nextInt()));



            }

            @Override
            protected void onUnBind() {

            }

        };
    }
}
