package com.timesanimation

import com.timesanimation.Utils.ResourceUtils
import com.timesanimation.Utils.StartSnapHelper
import com.timesanimation.adapter.segment.SampleSegmentView
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clumob.listitem.controller.source.ArraySource
import com.clumob.listitem.controller.source.ItemController
import com.clumob.listitem.controller.source.ItemControllerSource
import com.clumob.recyclerview.adapter.RvAdapter
import com.clumob.recyclerview.adapter.RvViewHolder
import com.clumob.recyclerview.adapter.ViewHolderProvider
import com.clumob.segment.controller.SegmentController
import com.clumob.segment.controller.SegmentInfo
import com.clumob.segment.controller.Storable
import com.clumob.segment.controller.list.SegmentItemControllerImpl
import com.clumob.segment.manager.Segment
import com.clumob.segment.manager.SegmentViewHolder
import com.clumob.segment.manager.SegmentViewHolderFactory
import com.clumob.segment.support.pager.recycler.SegmentItemViewHolder
import com.clumob.segment.support.pager.recycler.SegmentTabsHelper.attach
import com.google.android.material.tabs.TabLayout
import java.util.*

class ScrollingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var tabLayout: TabLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrolling)
        setupViews()
    }

    private fun setupViews() {
        toolbar = findViewById(R.id.toolbar)
        setupRecyclerView()
        setupToolbar()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun setupRecyclerView() {
        tabLayout = findViewById(R.id.tabs)
        tabLayout.setLayoutMode(TabLayout.MODE_SCROLLABLE)
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER)
        recyclerView = findViewById(R.id.recycler_view)
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setLayoutManager(mLayoutManager)
        recyclerView.setItemAnimator(DefaultItemAnimator())
        val startSnapHelper = StartSnapHelper()
        startSnapHelper.attachToRecyclerView(recyclerView)
        val itemControllerSource = createItemControllerSource()
        recyclerView.post(Runnable {
            recyclerView.setAdapter(RvAdapter(createViewHolderProvider(), itemControllerSource, this@ScrollingActivity))
            for (i in 0 until itemControllerSource.itemCount) {
                tabLayout.addTab(tabLayout.newTab().setText("Index $i"))
            }
        })
        attach(recyclerView, tabLayout, startSnapHelper)
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

    private fun createRvAdapter(): RecyclerView.Adapter<RvViewHolder<*>> {
        return RvAdapter(createViewHolderProvider(), createItemControllerSource(), this)
    }

    private fun createItemControllerSource(): ItemControllerSource<*> {
        val arraySource = ArraySource<SegmentItemControllerImpl>()
        val segmentItemControllerImpls: MutableList<SegmentItemControllerImpl> = ArrayList()
        for (i in 0..99) {
            segmentItemControllerImpls.add(object : SegmentItemControllerImpl(SegmentInfo(i, null)) {
                override fun getType(): Int {
                    return 0
                }

                override fun getId(): Long {
                    return 0
                }
            })
        }
        arraySource.setMaxLimit(4)
        arraySource.switchItems(segmentItemControllerImpls)
        return arraySource
    }

    private fun createViewHolderProvider(): ViewHolderProvider {
        return object : ViewHolderProvider() {
            override fun provideViewHolder(viewGroup: ViewGroup, type: Int): RvViewHolder<out ItemController> {
                val sampleSegmentView = SampleSegmentView(viewGroup.context, LayoutInflater.from(viewGroup.context), viewGroup)
                return object : SegmentItemViewHolder<Any?, SegmentController<Any?>>(sampleSegmentView.view, sampleSegmentView) {
                    override fun createSegment(segmentInfo: SegmentInfo?): Segment<*, *> {
                        return Segment(segmentInfo!!, object : SegmentController<Unit>{
                            override val viewData: Unit
                                get() = Unit

                            override fun onCreate(args: Storable?) {
                            }

                            override fun restoreState(restorableState: Storable?) {
                            }

                            override fun onStart() {
                            }

                            override fun onResume() {
                            }

                            override fun onPause() {
                            }

                            override fun onStop() {
                            }

                            override fun onDestroy() {
                            }

                        }, object : SegmentViewHolderFactory {
                            override fun create(context: Context, layoutInflater: LayoutInflater, parentView: ViewGroup?): SegmentViewHolder<*, *> {
                                return object : SegmentViewHolder<Unit, SegmentController<Unit>>(context,layoutInflater,parentView) {
                                    override fun createView(layoutInflater: LayoutInflater, viewGroup: ViewGroup?): View {
                                        return View(context)
                                    }

                                    override fun onBind() {
                                    }

                                    override fun onUnBind() {
                                    }
                                }
                            }

                        })
                    }

                    override fun onBindSegment() {}
                    override fun onUnbindSegment() {}
                }
            }

        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setTitle(ResourceUtils.getString(R.string.app_name))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_scrolling, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean { // Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }

    companion object {
        private const val MAX_OFFSET_LIMIT = 50f
        private const val MIN_OFFSET_LIMIT = 5f
        private const val TAG = "YOLOO"
    }
}