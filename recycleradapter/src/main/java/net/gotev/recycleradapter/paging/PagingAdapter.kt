package net.gotev.recycleradapter.paging

import android.os.Handler
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.NO_ID
import net.gotev.recycleradapter.*

class PagingAdapter(
    private val activity: FragmentActivity,
    recyclerDataSource: RecyclerDataSource<*, *>? = null,
    config: PagedList.Config = PagedList.Config.Builder().build(),
    emptyItem: AdapterItem<*>? = null,
    showEmptyItemOnStartup: Boolean = false,
    errorItem: AdapterItem<*>? = null
) : PagedListAdapter<AdapterItem<*>, RecyclerAdapterViewHolder>(diffCallback) {

    private val viewModel: PagedViewModel = ViewModelProviders.of(activity).get(PagedViewModel::class.java)

    init {
        viewModel.init(
            recyclerDataSource?.casted(),
            config,
            emptyItem,
            showEmptyItemOnStartup,
            errorItem
        )
        viewModel.data.observe(activity, Observer {
            it.observe(activity, Observer(::submitList))
        })

        if (showEmptyItemOnStartup) {
            Handler().post { reload() }
        }

        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterViewHolder {
        val item = currentList?.find { it.viewType() == viewType }
        return item?.createItemViewHolder(parent) ?: throw IllegalStateException("Item not found")
    }

    override fun onBindViewHolder(holder: RecyclerAdapterViewHolder, position: Int) {
        bindItem(holder, position, true)
    }

    override fun onBindViewHolder(
        holder: RecyclerAdapterViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        bindItem(holder, position, payloads.isEmpty())
    }

    override fun onCurrentListChanged(
        previousList: PagedList<AdapterItem<*>>?,
        currentList: PagedList<AdapterItem<*>>?
    ) {
        super.onCurrentListChanged(previousList, currentList)
        if (currentList.isNullOrEmpty()) {
            if (getState().value == LoadingState.ERROR) {
                showError()
            } else {
                clear()
            }
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).viewType()

    override fun getItemId(position: Int) =
        getItem(position)?.diffingId()?.hashCode()?.toLong() ?: NO_ID

    fun setDataSource(newDataSource: RecyclerDataSource<*, *>, config: PagedList.Config) {
        viewModel.swapDataSource(newDataSource.casted(), config)
    }

    fun setDataSource(newDataSource: RecyclerDataSource<*, *>, pageSize: Int) {
        viewModel.swapDataSource(newDataSource.casted(), pageSize)
    }

    fun setEmptyItem(item: AdapterItem<*>) {
        viewModel.setEmptyItem(item)
    }

    fun setErrorItem(item: AdapterItem<*>) {
        viewModel.setErrorItem(item)
    }

    fun reload() {
        viewModel.reload()
    }

    fun getState() = viewModel.loadingState

    fun clear() {
        viewModel.clear()
    }

    fun showError() {
        viewModel.showError()
    }

    private fun bindItem(holder: RecyclerAdapterViewHolder, position: Int, firstTime: Boolean) {
        getItem(position)?.castAsIn()?.bind(firstTime, holder)
            ?: throw IllegalStateException("Item not found")
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<AdapterItem<*>>() {
            override fun areItemsTheSame(oldItem: AdapterItem<*>, newItem: AdapterItem<*>) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: AdapterItem<*>, newItem: AdapterItem<*>) =
                !oldItem.hasToBeReplacedBy(oldItem)
        }
    }
}