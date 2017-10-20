package niks.foreignreader.data.datasource

import com.idea.engineering.ssska.data_layer.data_source.DataSourceListener

abstract class DataSourceDidLoadListener<T> : DataSourceListener<T> {
    override fun notifyWillLoadItems() {
        // not implemented
    }

    override fun notifyDidLoadItemsWithError(error: String) {
        // not implemented
    }
}