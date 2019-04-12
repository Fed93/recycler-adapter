package net.gotev.recycleradapter.paging

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.RecyclerAdapterViewHolder
import net.gotev.recycleradapter.castAsIn
import net.gotev.recycleradapter.viewType

class PagingAdapter(
    activity: FragmentActivity,
    recyclerDataSource: RecyclerDataSource<Any, AdapterItem<*>>,
    config: PagedList.Config
) : PagedListAdapter<AdapterItem<*>, RecyclerAdapterViewHolder>(diffCallback) {

    private val viewModel: PagedViewModel = ViewModelProviders.of(activity).get(PagedViewModel::class.java)
    private var emptyItem: AdapterItem<in RecyclerAdapterViewHolder>? = null

    init {
        viewModel.init(recyclerDataSource, config)
        viewModel.data.observe(activity, Observer { list ->
            submitList(list)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterViewHolder {
        val item = if (canShowEmptyItem() && viewType == emptyItem.viewType()) {
            emptyItem!!
        } else {
            currentList?.find { it.viewType() == viewType }
        }

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

    override fun getItemViewType(position: Int): Int {
        if (canShowEmptyItem()) {
            return emptyItem.viewType()
        }
        return getItem(position).viewType()
    }

    fun setEmptyItem(item: AdapterItem<*>?) {
        emptyItem = item?.castAsIn()
    }

    fun reload() {
        viewModel.reload()
    }

    fun getState() = viewModel.loadingState

    fun clear() {
        if (currentList?.isEmpty() == false) {
            submitList(null)
            notifyItemInserted(0)
        }
    }

    private fun canShowEmptyItem(): Boolean = false

    private fun bindItem(holder: RecyclerAdapterViewHolder, position: Int, firstTime: Boolean) {
        val item = if (canShowEmptyItem()) {
            emptyItem!!
        } else {
            getItem(position)?.castAsIn()
        }

        item?.bind(firstTime, holder) ?: throw IllegalStateException("Item not found")
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