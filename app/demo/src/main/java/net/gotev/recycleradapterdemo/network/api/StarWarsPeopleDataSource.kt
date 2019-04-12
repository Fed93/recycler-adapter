package net.gotev.recycleradapterdemo.network.api

import android.util.Log
import androidx.paging.PageKeyedDataSource
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.paging.LoadingState
import net.gotev.recycleradapter.paging.PagingResultCallback
import net.gotev.recycleradapter.paging.RecyclerDataSource
import net.gotev.recycleradapterdemo.adapteritems.TitleSubtitleItem

class StarWarsPeopleDataSource(private val api: StarWarsAPI) : RecyclerDataSource<String, AdapterItem<*>> {

    override fun loadInitial(params: PageKeyedDataSource.LoadInitialParams<String>, callback: PagingResultCallback<String, AdapterItem<*>>) {
        try {
            val response = api.getPeople().blockingGet()
            callback.onInitialResult(response.results.map { convert(it) }, response.previous, response.next)
        } catch (exc: Throwable) {
            Log.e("Error", "Error", exc)
            callback.onInitialResult(emptyList(), null, null, loadingState = LoadingState.ERROR)
        }
    }

    override fun loadAfter(params: PageKeyedDataSource.LoadParams<String>, callback: PagingResultCallback<String, AdapterItem<*>>) {
        load(params, callback)
    }

    override fun loadBefore(params: PageKeyedDataSource.LoadParams<String>, callback: PagingResultCallback<String, AdapterItem<*>>) {
        load(params, callback, isBefore = true)
    }

    private fun convert(model: SWAPIPerson): AdapterItem<*> {
        return TitleSubtitleItem(model.name, "Height (cm): ${model.height}")
    }

    private fun load(params: PageKeyedDataSource.LoadParams<String>,
                     callback: PagingResultCallback<String, AdapterItem<*>>,
                     isBefore: Boolean = false) {
        try {
            val response = api.getPeopleFromUrl(params.key).blockingGet()
            callback.onResult(
                    response.results.map { convert(it) },
                    if (isBefore) response.previous else response.next
            )
        } catch (exc: Throwable) {
            Log.e("Error", "Error", exc)
            callback.onResult(emptyList(), null, loadingState = LoadingState.ERROR)
        }
    }
}
