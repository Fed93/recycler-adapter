package net.gotev.recycleradapter.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import net.gotev.recycleradapter.AdapterItem

internal class PagedDataSourceFactory<T>(
    private val recyclerDataSource: RecyclerDataSource<T, AdapterItem<*>>,
    private val loadingState: MutableLiveData<LoadingState>
) : DataSource.Factory<T, AdapterItem<*>>() {

    internal val pagedDataSourceLiveData = MutableLiveData<PagedDataSource<T>>()

    override fun create(): DataSource<T, AdapterItem<*>> {
        val dataSource = PagedDataSource(recyclerDataSource, loadingState)
        pagedDataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}