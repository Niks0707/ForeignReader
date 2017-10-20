package com.idea.engineering.ssska.data_layer.data_source

import java.util.concurrent.CopyOnWriteArrayList

abstract class BaseDataSource<T> {

    protected var data: T? = null

    private val listeners = CopyOnWriteArrayList<DataSourceListener<T>>()

    abstract fun reloadData()

    fun addListener(listener: DataSourceListener<T>) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeListener(listener: DataSourceListener<T>) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
        }
    }

    internal fun notifyListenersDidLoadItems() {
        for (listener in listeners) {
            data?.let { listener.notifyDidLoadItems(data) }
        }
    }

    internal fun notifyListenersWillLoadItems() {
        for (listener in listeners) {
            listener.notifyWillLoadItems()
        }
    }

    internal fun notifyListenersDidLoadItemsWithError(error: String?) {
        for (listener in listeners) {
            error?.let { listener.notifyDidLoadItemsWithError(error) }
        }
    }
}
