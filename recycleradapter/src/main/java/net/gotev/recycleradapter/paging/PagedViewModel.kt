package net.gotev.recycleradapter.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import net.gotev.recycleradapter.AdapterItem

internal class PagedViewModel : ViewModel() {

    internal val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    private lateinit var pagedDataSourceFactory: PagedDataSourceFactory<Any>
    private lateinit var emptyDataSourceFactory: PagedDataSourceFactory<Int>

    private lateinit var fullData: LiveData<PagedList<AdapterItem<*>>>
    private lateinit var empty: LiveData<PagedList<AdapterItem<*>>>

    lateinit var data: LiveData<PagedList<AdapterItem<*>>>

    fun init(
        recyclerDataSource: RecyclerDataSource<Any, AdapterItem<*>>,
        config: PagedList.Config,
        emptyItem: AdapterItem<*>? = null,
        showEmptyItemOnStartup: Boolean = false
    ) {
        swapDataSource(recyclerDataSource, config)

        if (showEmptyItemOnStartup) {
            emptyItem?.let(::setEmptyItem) ?: throw IllegalStateException("EmptyItem null")
            clear()
        } else {
            emptyItem?.let(::setEmptyItem)
            dismissEmptyItem()
        }
    }

    fun setEmptyItem(emptyItem: AdapterItem<*>) {
        emptyDataSourceFactory = PagedDataSourceFactory(EmptyDataSource(emptyItem), loadingState)
        empty = emptyDataSourceFactory.toLiveData(1)
    }

    fun reload() {
        dismissEmptyItem()
        pagedDataSourceFactory.pagedDataSourceLiveData.value?.invalidate()
    }

    fun clear() {
        data = empty
    }

    private fun dismissEmptyItem() {
        data = fullData
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
        fullData = when {
            config != null -> pagedDataSourceFactory.toLiveData(config)
            pageSize != null -> pagedDataSourceFactory.toLiveData(pageSize)
            else -> throw IllegalStateException("Configuration and PageSize null")
        }
    }
}