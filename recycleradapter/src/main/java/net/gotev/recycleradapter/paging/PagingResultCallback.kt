package net.gotev.recycleradapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource

class PagingResultCallback<Key, Value>(
    private val state: MutableLiveData<LoadingState>,
    private val initialCallback: PageKeyedDataSource.LoadInitialCallback<Key, Value>? = null,
    private val callback: PageKeyedDataSource.LoadCallback<Key, Value>? = null
) {

    fun onInitialResult(
        data: List<Value>,
        position: Int,
        totalCount: Int,
        previousPageKey: Key?,
        nextPageKey: Key?,
        loadingState: LoadingState = LoadingState.DONE
    ) {
        initialCallback?.onResult(data, position, totalCount, previousPageKey, nextPageKey)
        state.postValue(loadingState)
    }

    fun onInitialResult(
        data: List<Value>,
        previousPageKey: Key?,
        nextPageKey: Key?,
        loadingState: LoadingState = LoadingState.DONE
    ) {
        initialCallback?.onResult(data, previousPageKey, nextPageKey)
        state.postValue(loadingState)
    }

    fun onResult(
        data: List<Value>,
        adjacentPageKey: Key?,
        loadingState: LoadingState = LoadingState.DONE
    ) {
        callback?.onResult(data, adjacentPageKey)
        state.postValue(loadingState)
    }
}