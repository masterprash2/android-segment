package com.clumob.segment.controller.list

import com.clumob.segment.controller.common.Controller
import com.clumob.segment.controller.common.ItemControllerWrapper

/**
 * Created by prashant.rathore on 17/12/18.
 */
class EmptyItemSource : ItemControllerSource() {
    override fun onAttachToView() {}
    override fun onItemAttached(position: Int) {}
    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getItemPosition(item: ItemControllerWrapper): Int {
        return 0
    }

    override fun getItemForPosition(position: Int): ItemControllerWrapper {
        throw Exception("Its an EmptySource. Should not come here $position")
    }

    override fun onDetachFromView() {}
    override fun computeItemCount(): Int {
        return 0
    }

    override fun destroy() {

    }
}