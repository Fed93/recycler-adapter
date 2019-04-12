package net.gotev.recycleradapter.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import net.gotev.recycleradapter.AdapterItem

internal class PagedViewModel : ViewModel() {

    private lateinit var pagedDataSourceFactory: PagedDataSourceFactory<Any>
    lateinit var data: LiveData<PagedList<AdapterItem<*>>>
    internal val loadingState: MutableLiveData<LoadingState> = MutableLiveData()

    internal val isListEmpty: Boolean
        get() = data.value?.isEmpty() ?: true

    fun init(
        recyclerDataSource: RecyclerDataSource<Any, AdapterItem<*>>,
        config: PagedList.Config
    ) {
        pagedDataSourceFactory = PagedDataSourceFactory(recyclerDataSource, loadingState)
        data = LivePagedListBuilder<Any, AdapterItem<*>>(pagedDataSourceFactory, config).build()
    }

    fun reload() {
        pagedDataSourceFactory.reload()
    }

    fun clear() {
        data.value?.detach()
    }
}