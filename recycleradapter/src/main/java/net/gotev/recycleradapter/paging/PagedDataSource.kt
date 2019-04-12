package net.gotev.recycleradapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import net.gotev.recycleradapter.AdapterItem

internal class PagedDataSource<T>(
    private val recyclerDataSource: RecyclerDataSource<T, AdapterItem<*>>,
    private val loadingState: MutableLiveData<LoadingState>
) : PageKeyedDataSource<T, AdapterItem<*>>() {

    override fun loadInitial(
        params: LoadInitialParams<T>,
        callback: LoadInitialCallback<T, AdapterItem<*>>
    ) {
        val internalCallback = PagingResultCallback(initialCallback = callback, state = loadingState)
        loadingState.postValue(LoadingState.INITIAL_LOADING)
        recyclerDataSource.loadInitial(params, internalCallback)
    }

    override fun loadAfter(params: LoadParams<T>, callback: LoadCallback<T, AdapterItem<*>>) {
        val internalCallback = PagingResultCallback(callback = callback, state = loadingState)
        loadingState.postValue(LoadingState.LOADING)
        recyclerDataSource.loadAfter(params, internalCallback)
    }

    override fun loadBefore(params: LoadParams<T>, callback: LoadCallback<T, AdapterItem<*>>) {
        val internalCallback = PagingResultCallback(callback = callback, state = loadingState)
        loadingState.postValue(LoadingState.LOADING)
        recyclerDataSource.loadBefore(params, internalCallback)
    }
}