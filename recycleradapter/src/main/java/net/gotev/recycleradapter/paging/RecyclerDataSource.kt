package net.gotev.recycleradapter.paging

import androidx.paging.PageKeyedDataSource

interface RecyclerDataSource<Key, Value> {

    fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Key>,
        callback: PagingResultCallback<Key, Value>
    )

    fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Key>,
        callback: PagingResultCallback<Key, Value>
    )

    fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Key>,
        callback: PagingResultCallback<Key, Value>
    )
}