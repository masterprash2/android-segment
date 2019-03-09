package com.clumob.segment.support.pager.viewpager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.clumob.listitem.controller.source.ItemController;
import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.listitem.controller.source.SourceUpdateEvent;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.support.pager.SegmentItemProvider;

import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import io.reactivex.observers.DisposableObserver;

import static com.clumob.segment.support.pager.viewpager.SegmentStatePagerAdapter.ItemSegmentPair.pair;

/**
 * Created by prashant.rathore on 02/07/18.
 */

public class SegmentStatePagerAdapter<T extends ItemController> extends SegmentPagerAdapter {

    private final ItemControllerSource<T> dataSource;
    private final SegmentItemProvider factory;
    private Set<ItemSegmentPair<T>> attachedSegments = new HashSet<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public SegmentStatePagerAdapter(ItemControllerSource<T> dataSource,
                                    SegmentItemProvider factory) {
        this.dataSource = dataSource;
        this.dataSource.setViewInteractor(createViewInteractor());
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

    private ItemControllerSource.ViewInteractor createViewInteractor() {
        return new ItemControllerSource.ViewInteractor() {
            Deque<Runnable> deque = new LinkedList();
            private boolean processingInProgress;

            public void processWhenSafe(Runnable runnable) {
                this.deque.add(runnable);
                if (!this.processingInProgress) {
                    this.processingInProgress = true;
                    this.processWhenQueueIdle();
                }

            }

            private void processWhenQueueIdle() {
                mHandler.post(new Runnable() {
                    public void run() {
                        if (isComputingLayout()) {
                            mHandler.post(this);
                        } else if (deque.peekFirst() != null) {
                            Runnable runnable = (Runnable) deque.pollFirst();
                            runnable.run();
                            mHandler.post(this);
                        } else {
                            processingInProgress = false;
                        }

                    }
                });
            }

            public void cancelOldProcess(Runnable runnable) {
                mHandler.removeCallbacks(runnable);
            }
        };
    }

    private boolean isComputingLayout() {
        return false;
    }

    @Override
    public Object instantiateItem(int index) {
        T item = getItem(index);
        Segment<?, ?> segment = factory.provide(item);
        ItemSegmentPair pair = pair(segment, item);
        attachedSegments.add(pair);
        segment.onCreate();
        dataSource.onItemAttached(index);
        pair.itemController.onAttach(pair.segment);
        return pair;
    }

    @Override
    public int getCount() {
        return dataSource.getItemCount();
    }

    public Set<ItemSegmentPair<T>> getAttachedSegments() {
        return attachedSegments;
    }

    @Override
    protected Segment<?, ?> retrieveSegmentFromObject(Object object) {
        return ((ItemSegmentPair) object).segment;
    }

    // ToDO: The lookup algorightm is slow;
    @Override
    public int computeItemPosition(Object inputItem) {
        final long id = ((ItemSegmentPair) inputItem).itemController.getId();
        int itemCount = dataSource.getItemCount();
        for (int i = 0; i < itemCount; i++) {
            T item = getItem(i);
            if (item.getId() == id) {
                return i;
            }
        }
        return POSITION_NONE;
    }


    @Override
    public void destroyItem(Object object) {
        super.destroyItem(object);
        ItemSegmentPair pair = (ItemSegmentPair) object;
        pair.itemController.onDetach(pair.segment);
        pair.segment.onDestroy();
        attachedSegments.remove(pair);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
        for (ItemSegmentPair<T> segment : attachedSegments) {
            segment.segment.onCreate();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (ItemSegmentPair<T> segment : attachedSegments) {
            segment.segment.onStart();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (ItemSegmentPair<T> segment : attachedSegments) {
            segment.segment.onPause();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outBundle) {
//        for(Segment segment : attachedSegments) {
//        }
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        for (ItemSegmentPair<T> segment : attachedSegments) {
            segment.segment.onConfigurationChanged(configuration);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (ItemSegmentPair<T> segment : attachedSegments) {
            segment.segment.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (ItemSegmentPair<T> segment : attachedSegments) {
            segment.segment.onDestroy();
        }
    }

    public T getItem(int position) {
        return dataSource.getItem(position);
    }

    public ItemControllerSource<T> getDataSource() {
        return dataSource;
    }

    static class ItemSegmentPair<T extends ItemController> {

        final Segment<?, ?> segment;
        final T itemController;

        public ItemSegmentPair(Segment<?, ?> segment, T itemController) {
            this.segment = segment;
            this.itemController = itemController;
        }

        static <T extends ItemController> ItemSegmentPair<T> pair(Segment<?, ?> segment, T itemController) {
            return new ItemSegmentPair<T>(segment, itemController);
        }
    }
}
