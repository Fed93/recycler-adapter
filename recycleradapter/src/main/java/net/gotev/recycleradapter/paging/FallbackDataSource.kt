package net.gotev.recycleradapter.paging

import androidx.paging.PageKeyedDataSource
import net.gotev.recycleradapter.AdapterItem

internal class FallbackDataSource(item: AdapterItem<*>? = null) : RecyclerDataSource<Int, AdapterItem<*>> {

    private val fallbackData = mutableListOf<AdapterItem<*>>()

    init {
        item?.let(fallbackData::add)
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
        callback.onInitialResult(fallbackData, null, null)
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) { }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) { }
}