package com.clumob.segment.pager;

import android.support.annotation.NonNull;
import android.view.View;

import com.clumob.list.presenter.source.Presenter;
import com.clumob.list.presenter.source.PresenterSource;
import com.clumob.list.presenter.source.SourceUpdateEvent;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.presenter.SegmentInfo;
import com.clumob.segment.presenter.SegmentPagerItemPresenter;
import com.clumob.segment.screen.SegmentView;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class SegmentStatePagerAdapter extends SegmentPagerAdapter {

    private final PresenterSource<? extends Presenter> dataSource;
    private final SegmentControllerFactory factory;

    public SegmentStatePagerAdapter(PresenterSource<? extends Presenter> dataSource, SegmentControllerFactory factory) {
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
    public SegmentController<?> instantiateItem(int index) {
        SegmentPagerItemPresenter item = (SegmentPagerItemPresenter) dataSource.getItem(index);
        SegmentController<?> segmentController = factory.create(item.viewModel);
        segmentController.onCreate();
        return segmentController;
    }

    @Override
    public int getCount() {
        return dataSource.getItemCount();
    }


    // ToDO: The lookup algorightm is slow;
    @Override
    public int computeItemPosition(SegmentController<?> segmentController) {
        SegmentInfo segmentInfo = segmentController.getSegmentInfo();
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
        SegmentController segmentController = (SegmentController) object;
        SegmentView boundedView = segmentController.getBoundedView();
        return boundedView != null && boundedView.getView() == view;
    }

    @Override
    public void destroyItem(SegmentController<?> segmentController) {
        super.destroyItem(segmentController);
        segmentController.onDestroy();
    }
}
