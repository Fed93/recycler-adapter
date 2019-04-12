package net.gotev.recycleradapter.paging

import androidx.paging.PageKeyedDataSource
import net.gotev.recycleradapter.AdapterItem

class EmptyDataSource(
    emptyItem: AdapterItem<*>? = null
) : RecyclerDataSource<Int, AdapterItem<*>> {

    private val fallbackData = mutableListOf<AdapterItem<*>>()

    init {
        emptyItem?.let(fallbackData::add)
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

    fun setEmptyState(emptyItem: AdapterItem<*>) {
        if (!fallbackData.contains(emptyItem)) {
            fallbackData.add(emptyItem)
        }
    }
}