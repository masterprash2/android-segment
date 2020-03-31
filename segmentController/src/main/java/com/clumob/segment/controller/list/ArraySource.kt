package com.clumob.segment.controller.list

import com.clumob.segment.controller.common.Controller
import com.clumob.segment.controller.common.ItemControllerWrapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by prashant.rathore on 24/06/18.
 */
class ArraySource : ItemControllerSource() {
    private var controller: MutableList<ItemControllerWrapper> = ArrayList()
    private var isAttached = false
    private val itemUpdatePublisher = ItemUpdatePublisher()
    private var compositeDisposable: CompositeDisposable? = null
    override fun onAttachToView() {
        isAttached = true
        compositeDisposable = CompositeDisposable()
        compositeDisposable!!.add(observeItemUpdates())
        for (item in controller) {
            item.performCreate(itemUpdatePublisher)
        }
    }

    override fun onItemAttached(position: Int) {}
    val items: List<ItemControllerWrapper>
        get() = controller

    fun setItems(items: List<Controller>?) {
        switchItems(items)
    }

    private fun switchItems(items: List<Controller>?, useDiffProcess: Boolean) {
        val newItems = ArrayList(items ?: listOf())
        processWhenSafe(Runnable { switchItemImmediate(useDiffProcess, newItems) })
    }

    private fun switchItemImmediate(useDiffProcess: Boolean, items: MutableList<Controller>) {
        var newItems = items.map { ItemControllerWrapper(it) }.toMutableList()
        val oldCount = controller.size
        val newCount = newItems.size
        val retained: MutableSet<ItemControllerWrapper> = HashSet()
        val diffResult = diffResults(controller, newItems, retained)
        val oldItems = controller
        controller = newItems
        beginUpdates()
        if (useDiffProcess) {
            diffResult.dispatchUpdatesTo(this@ArraySource)
        } else {
            val diff = newCount - oldCount
            if (diff > 0) {
                notifyItemsInserted(oldCount, diff)
                notifyItemsChanged(0, oldCount)
            } else if (diff < 0) {
                notifyItemsRemoved(newCount, diff * -1)
                notifyItemsChanged(0, newCount)
            } else {
                notifyItemsChanged(0, newCount)
            }
        }
        endUpdates()
        if (isAttached) {
            newItems.onEach { it.performCreate(itemUpdatePublisher) }
        }

        oldItems.removeAll(retained)
        oldItems.onEach { it.performDestroy() }
    }

    fun switchItems(items: List<Controller>?) {
        switchItems(items, false)
    }

    fun switchItemsWithDiffRemovalAndInsertions(items: MutableList<Controller>?) {
        switchItems(items, true)
    }

    private fun diffResults(oldItems: List<ItemControllerWrapper>, newItems: MutableList<ItemControllerWrapper>, retained: MutableSet<ItemControllerWrapper>): DiffUtil.DiffResult {
        return DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return oldItems.size
            }

            override fun getNewListSize(): Int {
                return newItems.size
            }

            override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean {
                val itemOld = oldItems[oldPosition]
                val itemNew = newItems[newPosition]
                val equals = itemOld.controller === itemNew.controller
                        || itemOld.controller.hashCode() == itemNew.controller.hashCode() && itemOld.controller == itemNew.controller
                if (equals) {
                    newItems[newPosition] = itemOld
                    retained.add(itemOld)
                }
                return equals
            }

            override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
                return areItemsTheSame(oldPosition, newPosition)
            }
        }, false)
    }

    fun replaceItem(index: Int, item: Controller) {
        processWhenSafe(Runnable { replaceItemWhenSafe(index, ItemControllerWrapper(item)) })
    }

    private fun replaceItemWhenSafe(index: Int, item: ItemControllerWrapper) {
        val set = controller.set(index, item)
        set.performDestroy()
        notifyItemsChanged(index, 1)
        if (isAttached) {
            item.performCreate(itemUpdatePublisher)
        }
    }

    override fun getItemPosition(item: ItemControllerWrapper): Int {
        return controller.indexOfFirst { it === item }
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun computeItemCount(): Int {
        return controller.size
    }

    override fun getItemForPosition(position: Int): ItemControllerWrapper {
        return controller[position]
    }

    //    @Override
//    public void onItemDetached(int position) {
//
//    }
    private fun observeItemUpdates(): Disposable {
        return itemUpdatePublisher.observeEvents().subscribe { itemController: ItemControllerWrapper -> postItemUpdate(itemController) }
    }

    private fun postItemUpdate(itemController: ItemControllerWrapper) {
        processWhenSafe(Runnable {
            val index = controller.indexOf(itemController)
            if (index >= 0) notifyItemsChanged(index, 1)
        })
    }

    override fun onDetachFromView() {
        compositeDisposable!!.dispose()
        isAttached = false
        controller.onEach { it.performDestroy() }
    }
}