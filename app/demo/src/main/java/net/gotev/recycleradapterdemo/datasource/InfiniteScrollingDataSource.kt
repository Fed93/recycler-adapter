package net.gotev.recycleradapterdemo.datasource

import androidx.paging.PageKeyedDataSource
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.paging.PagingResultCallback
import net.gotev.recycleradapter.paging.RecyclerDataSource
import net.gotev.recycleradapterdemo.adapteritems.TextWithToggleItem
import net.gotev.recycleradapterdemo.adapteritems.TitleSubtitleItem
import net.gotev.recycleradapterdemo.adapteritems.leavebehind.MyLeaveBehindItem
import java.util.Random

class InfiniteScrollingDataSource : RecyclerDataSource<Int, AdapterItem<*>> {

    private val random by lazy {
        Random(System.currentTimeMillis())
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
        val final = mutableListOf<AdapterItem<*>>()

        final.add(MyLeaveBehindItem("swipe to left to leave behind", "option"))

        // add many items of two kinds
        val items = (0..random.nextInt(200) + 50).map {
            if (it % 2 == 0)
                TitleSubtitleItem("Item $it")
            else
                TextWithToggleItem("Toggle $it")
        }

        final.addAll(items)
        callback.onInitialResult(final, null, 1)
    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) { }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PagingResultCallback<Int, AdapterItem<*>>
    ) {
        // add many items of two kinds
        val items = (0..random.nextInt(200) + 50).map {
            if (it % 2 == 0)
                TitleSubtitleItem("Item ${it + params.key * 1000}")
            else
                TextWithToggleItem("Toggle ${it + params.key * 1000}")
        }

        callback.onResult(items, params.key + 1)
    }

}