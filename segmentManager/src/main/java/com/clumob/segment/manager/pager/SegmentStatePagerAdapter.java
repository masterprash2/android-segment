package com.clumob.segment.manager.pager;

import android.support.annotation.NonNull;
import android.view.View;

import com.clumob.list.presenter.source.Presenter;
import com.clumob.list.presenter.source.PresenterSource;
import com.clumob.list.presenter.source.SourceUpdateEvent;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.SegmentPagerItemPresenter;
import com.clumob.segment.manager.SegmentViewHolder;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class SegmentStatePagerAdapter extends SegmentPagerAdapter {

    private final PresenterSource<? extends Presenter> dataSource;
    private final SegmentPagerItemFactory factory;

    public SegmentStatePagerAdapter(PresenterSource<? extends Presenter> dataSource, SegmentPagerItemFactory factory) {
        this.dataSource = dataSource;
        this.factory = factory;
        this.dataSource.observeAdapterUpdates().subscribe(new DisposableObserver<SourceUpdateEvent>() {
            @Override
            public void onNext(SourceUpdateEvent sourceUpdateEvent) {
                if (sourceUpdateEvent.getType() == SourceUpdateEvent.Type.UPDATE_ENDS)
                    notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public Segment<?,?,?> instantiateItem(int index) {
        SegmentPagerItemPresenter item = (SegmentPagerItemPresenter) dataSource.getItem(index);
        Segment<?,?,?> segment = factory.create(item.viewModel);
        segment.onCreate();
        return segment;
    }

    @Override
    public int getCount() {
        return dataSource.getItemCount();
    }


    // ToDO: The lookup algorightm is slow;
    @Override
    public int computeItemPosition(Segment<?,?,?> segment) {
        SegmentInfo segmentInfo = segment.getSegmentInfo();
        int itemCount = dataSource.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            SegmentPagerItemPresenter item = (SegmentPagerItemPresenter) dataSource.getItem(i);
            if (item.viewModel.getId() == segmentInfo.getId()) {
                return i;
            }
        }
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        Segment segment = (Segment) object;
        SegmentViewHolder boundedView = segment.getBoundedView();
        return boundedView != null && boundedView.getView() == view;
    }

    @Override
    public void destroyItem(Segment<?,?,?> segment) {
        super.destroyItem(segment);
        segment.onDestroy();
    }
}
