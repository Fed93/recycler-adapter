package net.gotev.recycleradapter

import androidx.lifecycle.MutableLiveData
import net.gotev.recycleradapter.paging.RecyclerDataSource

/**
 * @author Aleksandar Gotev
 */
internal fun AdapterItem<*>?.viewType() = this?.javaClass?.name?.hashCode() ?: 0
internal fun Class<out AdapterItem<*>>.viewType() = hashCode()

@Suppress("UNCHECKED_CAST")
internal fun <T : RecyclerAdapterViewHolder> AdapterItem<out T>.castAsIn(): AdapterItem<in T> {
    return this as AdapterItem<in T>
}

@Suppress("UNCHECKED_CAST")
internal fun RecyclerDataSource<*, *>.casted() = this as RecyclerDataSource<Any, AdapterItem<*>>

internal fun <T, E> MutableLiveData<T>.swapWith(`in`: E) {
    postValue(`in` as T)
}