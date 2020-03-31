package com.clumob.segment.controller.common

import com.clumob.segment.controller.list.ItemUpdatePublisher
import java.util.*

/**
 * Created by prashant.rathore on 20/06/18.
 */
class ItemControllerWrapper(val controller: Controller) {

    enum class State {
        FRESH, CREATE, START, RESUME, PAUSE, STOP, DESTROY
    }

    var state = State.FRESH
        private set

    private var itemUpdatePublisher: ItemUpdatePublisher? = null

    fun type() = controller.getType()
    fun id() = controller.getId()

    private val attachedSources: MutableSet<Any> = HashSet()

    fun performCreate(itemUpdatePublisher: ItemUpdatePublisher) {
        this.itemUpdatePublisher = itemUpdatePublisher
        when (state) {
            State.FRESH, State.DESTROY -> {
                state = State.CREATE
                onCreate()
            }
            else -> return
        }
    }

    protected open fun onCreate() {
        controller.onCreate()
    }

    fun performStart(source: Any) {
        performCreate(itemUpdatePublisher!!)
        if (attachedSources.size > 0) {
            attachedSources.add(source)
            return
        }
        attachedSources.add(source)
        when (state) {
            State.CREATE, State.STOP -> {
                state = State.START
                onStartAttach()
            }
        }
    }

    protected open fun onStartAttach() {
        controller.onStart()
    }

    fun performResume() {
        performStart(attachedSources.iterator().next())
        when (state) {
            State.START, State.PAUSE -> {
                state = State.RESUME
                onResume()
            }
        }
    }

    protected open fun onResume() {
        controller.onResume()
    }

    fun performPause() {
        performStart(attachedSources.iterator().next())
        when (state) {
            State.RESUME -> {
                state = State.PAUSE
                onPause()
            }
        }
    }

    protected open fun onPause() {
        controller.onPause()
    }

    fun performStop(source: Any?) {
        attachedSources.remove(source)
        if (attachedSources.isNotEmpty()) return
        when (state) {
            State.START, State.PAUSE -> {
                state = State.STOP
                onStopDetach()
            }
            State.RESUME -> {
                performPause()
                state = State.STOP
                onStopDetach()
            }
        }
    }

    protected open fun onStopDetach() {
        controller.onStop()
    }

    fun performDestroy() {
        when (state) {
            State.CREATE, State.STOP -> {
                state = State.DESTROY
                onDestroy()
            }
            State.START, State.PAUSE, State.RESUME -> {
                attachedSources.clear()
                performStop(null)
                state = State.DESTROY
                onDestroy()
            }
        }
        itemUpdatePublisher = null
    }


    protected open fun onDestroy() {
        controller.onDestroy()
    }

}