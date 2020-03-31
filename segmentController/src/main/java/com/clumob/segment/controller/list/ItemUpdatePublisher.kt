package com.clumob.segment.controller.list

import com.clumob.segment.controller.common.ItemControllerWrapper
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

/**
 * Created by prashant.rathore on 20/09/18.
 */
class ItemUpdatePublisher {
    private val updateEventPublisher = BehaviorSubject.create<ItemControllerWrapper>()
    fun observeEvents(): Observable<ItemControllerWrapper> {
        return updateEventPublisher
    }

    fun notifyItemUpdated(itemController: ItemControllerWrapper) {
        updateEventPublisher.onNext(itemController)
    }
}