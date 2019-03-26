package in.til.yogeshkumar.timesanimation;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.clumob.listitem.controller.source.ArraySource;
import com.clumob.listitem.controller.source.ItemControllerSource;
import com.clumob.recyclerview.adapter.RvAdapter;
import com.clumob.recyclerview.adapter.RvViewHolder;
import com.clumob.recyclerview.adapter.ViewHolderProvider;
import com.clumob.segment.controller.SegmentController;
import com.clumob.segment.controller.SegmentInfo;
import com.clumob.segment.controller.list.SegmentItemControllerImpl;
import com.clumob.segment.manager.Segment;
import com.clumob.segment.support.pager.recycler.SegmentItemViewHolder;
import com.clumob.segment.support.pager.recycler.SegmentTabsHelper;

import java.util.ArrayList;
import java.util.List;

import in.til.yogeshkumar.timesanimation.Utils.ResourceUtils;
import in.til.yogeshkumar.timesanimation.Utils.StartSnapHelper;
import in.til.yogeshkumar.timesanimation.adapter.segment.SampleSegmentView;
import io.reactivex.subjects.BehaviorSubject;

public class ScrollingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private final static float MAX_OFFSET_LIMIT = 50f;
    private final static float MIN_OFFSET_LIMIT = 5f;
    private final static String TAG = "YOLOO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        setupViews();
    }

    private void setupViews() {
        toolbar = findViewById(R.id.toolbar);
        setupRecyclerView();
        setupToolbar();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setupRecyclerView() {
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setLayoutMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final StartSnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(recyclerView);


        final ItemControllerSource itemControllerSource = createItemControllerSource();
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(new RvAdapter(createViewHolderProvider(), itemControllerSource, ScrollingActivity.this));
                for(int i = 0 ; i < itemControllerSource.getItemCount() ; i++) {
                    tabLayout.addTab(tabLayout.newTab().setText("Index " + i));
                }
            }
        });

        SegmentTabsHelper.attach(recyclerView, tabLayout, startSnapHelper);

//        RecyclerView.Adapter adapter = recyclerView.getAdapter();
//        for(int i = 0 ; i < adapter.getItemCount() ; i++) {
//            tabLayout.addTab(tabLayout.newTab().setText("Index "+ i));
//        }
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                segmentPageChangeHelper.scrollToPosition(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        segmentPageChangeHelper.addPageChangeListner(new SegmentPageChangeHelper.PageChangeListner() {
//            @Override
//            public void onPageChanged(int position) {
//                tabLayout.getTabAt(position).select();
//            }
//
//            @Override
//            public void onPageScrolled(int position, float offset) {
//                tabLayout.setScrollPosition(position,offset,true);
//            }
//        });


    }

    private RecyclerView.Adapter<RvViewHolder> createRvAdapter() {
        return new RvAdapter(createViewHolderProvider(), createItemControllerSource(), this);
    }

    private ItemControllerSource createItemControllerSource() {
        ArraySource<SegmentItemControllerImpl> arraySource = new ArraySource<>();

        List<SegmentItemControllerImpl> segmentItemControllerImpls = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            segmentItemControllerImpls.add(new SegmentItemControllerImpl(new SegmentInfo(i, null)) {

                @Override
                public int getType() {
                    return 0;
                }

                @Override
                public long getId() {
                    return 0;
                }
            });

        }
        arraySource.setMaxLimit(4);
        arraySource.switchItems(segmentItemControllerImpls);
        return arraySource;
    }

    private ViewHolderProvider createViewHolderProvider() {
        return new ViewHolderProvider() {
            @Override
            public SegmentItemViewHolder<?, ? extends SegmentController> provideViewHolder(ViewGroup viewGroup, int i) {
                SampleSegmentView sampleSegmentView = new SampleSegmentView(viewGroup.getContext(), LayoutInflater.from(viewGroup.getContext()), viewGroup);
                return new SegmentItemViewHolder(sampleSegmentView.getView(),sampleSegmentView) {
                    @Override
                    protected Segment createSegment(SegmentInfo segmentInfo) {
                        return null;
                    }

                    @Override
                    protected void onBindSegment() {

                    }

                    @Override
                    protected void onUnbindSegment() {

                    }
                };
            }
        };
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(ResourceUtils.getString(R.string.app_name));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
