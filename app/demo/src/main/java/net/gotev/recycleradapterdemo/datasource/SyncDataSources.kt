package net.gotev.recycleradapterdemo.datasource

import androidx.paging.PageKeyedDataSource
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.paging.PagingResultCallback
import net.gotev.recycleradapter.paging.RecyclerDataSource
import net.gotev.recycleradapterdemo.adapteritems.LabelItem
import net.gotev.recycleradapterdemo.adapteritems.SyncItem
import kotlin.random.Random

class SyncDataSourceA : RecyclerDataSource<Int, AdapterItem<*>> {

    private val list = arrayListOf(
        SyncItem(1, "listA"),
        SyncItem(2, "listA")
    )

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
        callback.onInitialResult(list, null, null)
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

}

class SyncDataSourceB : RecyclerDataSource<Int, AdapterItem<*>> {

    private val list = arrayListOf(
        SyncItem(1, "listA"),
        SyncItem(3, "listB"),
        SyncItem(4, "listB"),
        SyncItem(5, "listB")
    )

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
        list.add(SyncItem(list.last().id + 1, "listB${list.last().id + 1}"))
        list.add(SyncItem(list.last().id + 1, "listB${list.last().id + 1}"))

        callback.onInitialResult(list, null, null)
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

}

class SyncDataSourceC : RecyclerDataSource<Int, AdapterItem<*>> {

    private val list = arrayListOf(SyncItem(1, "listC"))

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
        callback.onInitialResult(list, null, null)
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

}

class SyncDataSourceShuffle : RecyclerDataSource<Int, AdapterItem<*>> {

    private val list = (0..Random.nextInt(2, 20)).flatMap {
        listOf(LabelItem("TITLE $it"), SyncItem(it, "ListC"))
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
        callback.onInitialResult(list, null, null)
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
    }

}