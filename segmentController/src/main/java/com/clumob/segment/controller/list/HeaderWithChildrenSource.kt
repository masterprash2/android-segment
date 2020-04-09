package com.clumob.segment.controller.list

import com.clumob.segment.controller.common.Controller
import com.clumob.segment.controller.common.ItemControllerWrapper
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by prashant.rathore on 24/06/18.
 */
class HeaderWithChildrenSource : ItemControllerSource() {
    private val headerItemSource: AdapterAsItem? = null
    private val childrenItemSource: AdapterAsItem? = null
    private var isAttached = false

    override fun onAttachToView() {
        isAttached = true
        headerItemSource?.adapter?.onAttachToView()
        childrenItemSource?.adapter?.onAttachToView()
    }

    override var viewInteractor: ViewInteractor? = null
        set(viewInteractor) {
            field = viewInteractor
            if (headerItemSource != null) headerItemSource.adapter.viewInteractor = viewInteractor
            if (childrenItemSource != null) {
                childrenItemSource.adapter.viewInteractor = viewInteractor
            }
        }

    override fun onItemAttached(position: Int) {
        val adapterAsItem = decodeAdapterItem(position)
        adapterAsItem!!.adapter.onItemAttached(position - adapterAsItem.startPosition)
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    //    public void addAdapter(ItemControllerSource<? extends ItemController> adapter) {
//        AdapterAsItem item = new AdapterAsItem(adapter);
//        if (adapters.size() > 0) {
//            AdapterAsItem previousItem = adapters.get(adapters.size() - 1);
//            item.startPosition = previousItem.startPosition + previousItem.adapter.getItemCount();
//        }
//        adapters.add(item);
//        if(isAttached) {
//            item.adapter.onAttached();
//        }
//        notifyItemsInserted(item.startPosition, item.adapter.getItemCount());
//    }
    override fun computeItemCount(): Int {
        return headerCount + childrentCount
    }

    override fun getItemForPosition(position: Int): ItemControllerWrapper {
        val item = decodeAdapterItem(position)
        return item!!.adapter.getItem(position - item.startPosition)
    }

    //    @Override
//    public void onItemDetached(int position) {
//        AdapterAsItem adapterAsItem = decodeAdapterItem(position);
//        adapterAsItem.adapter.onItemDetached(position - adapterAsItem.startPosition);
//    }
    override fun onDetachFromView() {
        headerItemSource?.adapter?.onDetachFromView()
        childrenItemSource?.adapter?.onDetachFromView()
        isAttached = false
    }

    private fun decodeAdapterItem(position: Int): AdapterAsItem? {
        return if (childrenItemSource != null && childrenItemSource.startPosition < position) {
            childrenItemSource
        } else {
            headerItemSource
        }
    }

    override fun getItemPosition(item: ItemControllerWrapper): Int {
        val top = 0
        var itemPosition = getItemPositionHeader(item)
        if (itemPosition < 0) {
            itemPosition = getItemPositionChildren(item)
        }
        return itemPosition
    }

    private fun getItemPositionHeader(item: ItemControllerWrapper): Int {
        return headerItemSource?.adapter?.getItemPosition(item) ?: -1
    }

    fun getItemPositionChildren(item: ItemControllerWrapper): Int {
        return if (childrenItemSource == null) {
            -1
        } else {
            childrenItemSource.adapter.getItemPosition(item) - childrenItemSource.startPosition
        }
    }

    fun updateIndexes(modifiedItem: AdapterAsItem) {
        if (modifiedItem === headerItemSource && childrenItemSource != null) {
            childrenItemSource.startPosition = headerCount
        }
    }

    private val childrentCount: Int
        get() = childrenItemSource?.adapter?.itemCount ?: 0

    private val headerCount: Int
        get() = headerItemSource?.adapter?.itemCount ?: 0

    inner class AdapterAsItem(val adapter: ItemControllerSource) {
        var startPosition = 0
        val updateObserver: Disposable
        fun transformUpdateEvent(event: SourceUpdateEvent) {
            val actualStartPosition = startPosition + event.position
            when (event.type) {
                SourceUpdateEvent.Type.UPDATE_BEGINS -> beginUpdates()
                SourceUpdateEvent.Type.ITEMS_CHANGED -> notifyItemsChanged(actualStartPosition, event.itemCount)
                SourceUpdateEvent.Type.ITEMS_REMOVED -> notifyItemsRemoved(actualStartPosition, event.itemCount)
                SourceUpdateEvent.Type.ITEMS_ADDED -> notifyItemsInserted(actualStartPosition, event.itemCount)
                SourceUpdateEvent.Type.ITEMS_MOVED -> {
                }
                SourceUpdateEvent.Type.UPDATE_ENDS -> endUpdates()
                SourceUpdateEvent.Type.HAS_STABLE_IDS -> {
                }
            }
            updateIndexes(this)
        }

        init {
            updateObserver = adapter.observeAdapterUpdates().subscribe(Consumer { event: SourceUpdateEvent -> transformUpdateEvent(event) } as Consumer<SourceUpdateEvent>)
        }
    }

    override fun destroy() {
        onDetachFromView()
        headerItemSource?.adapter?.destroy()
        childrenItemSource?.adapter?.destroy()
    }
}