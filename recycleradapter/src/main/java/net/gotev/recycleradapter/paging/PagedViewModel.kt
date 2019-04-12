package net.gotev.recycleradapter.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import net.gotev.recycleradapter.AdapterItem

internal class PagedViewModel : ViewModel() {

    private lateinit var pagedDataSourceFactory: PagedDataSourceFactory<Any>

    private val emptyDataSource = EmptyDataSource()

    lateinit var data: LiveData<PagedList<AdapterItem<*>>>
    internal val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    fun init(
        recyclerDataSource: RecyclerDataSource<Any, AdapterItem<*>>,
        config: PagedList.Config,
        emptyItem: AdapterItem<*>? = null
    ) {
        emptyItem?.let(emptyDataSource::setEmptyState)
        swapDataSource(recyclerDataSource, config)
    }

    fun setEmptyItem(emptyItem: AdapterItem<*>) {
        emptyDataSource.setEmptyState(emptyItem)
    }

    fun reload() {
        pagedDataSourceFactory.reload()
    }

    fun clear() {
        swapDataSource(emptyDataSource as RecyclerDataSource<Any, AdapterItem<*>>, 1)
    }

    fun swapDataSource(
        newDataSource: RecyclerDataSource<Any, AdapterItem<*>>,
        config: PagedList.Config
    ) {
        swapDataSource(newDataSource, config, null)
    }

    fun swapDataSource(newDataSource: RecyclerDataSource<Any, AdapterItem<*>>, pageSize: Int) {
        swapDataSource(newDataSource, null, pageSize)
    }

    private fun swapDataSource(
        newDataSource: RecyclerDataSource<Any, AdapterItem<*>>,
        config: PagedList.Config?,
        pageSize: Int?
    ) {
        pagedDataSourceFactory = PagedDataSourceFactory(newDataSource, loadingState)
        data = when {
            config != null -> pagedDataSourceFactory.toLiveData(config)
            pageSize != null -> pagedDataSourceFactory.toLiveData(pageSize)
            else -> throw IllegalStateException("Configuration and PageSize null")
        }
    }
}