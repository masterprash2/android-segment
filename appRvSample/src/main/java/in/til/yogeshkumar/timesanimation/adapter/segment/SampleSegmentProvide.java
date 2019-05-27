package in.til.yogeshkumar.timesanimation.adapter.segment;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.clumob.listitem.controller.source.ItemController;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentControllerImpl;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.manager.SegmentViewHolder;
import com.clumob.segment.manager.SegmentViewHolderFactory;
import com.clumob.segment.support.pager.SegmentItemProvider;

/**
 * Created by prashant.rathore on 11/07/18.
 */

public class SampleSegmentProvide implements SegmentItemProvider {
    @Override
    public Segment<?, ?> provide(final ItemController itemController) {
        final SegmentInfo segmentInfo = new SegmentInfo((int) itemController.getId(), null);
        return new Segment<>(segmentInfo, new SegmentControllerImpl<Object>(null, null){
            @Override
            public void onResume() {
                Log.d("SEGMENTRV","Resume INDEX- "+segmentInfo.getId());
                super.onResume();
            }

            @Override
            public void onPause() {
                Log.d("SEGMENTRV","Pause INDEX- "+segmentInfo.getId());
                super.onPause();
            }
        }, new SegmentViewHolderFactory() {
            @Override
            public SegmentViewHolder<?, ?> create(Context context, LayoutInflater layoutInflater, @Nullable ViewGroup parentView) {
                return new SampleSegmentView(context,layoutInflater,parentView);
            }
        });
    }

}
