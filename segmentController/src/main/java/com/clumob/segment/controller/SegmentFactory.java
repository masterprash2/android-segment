package com.clumob.segment.controller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.segment.screen.SegmentView;

public interface SegmentFactory {
    SegmentView<?,?> create(Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView);
}
