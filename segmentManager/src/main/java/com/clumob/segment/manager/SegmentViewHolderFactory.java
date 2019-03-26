package com.clumob.segment.manager;

import android.content.Context;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface SegmentViewHolderFactory {
    SegmentViewHolder<?,?> create(Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView);
}
