package net.gotev.recycleradapterdemo.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.RecyclerAdapter
import net.gotev.recycleradapter.paging.PagingAdapter
import net.gotev.recycleradapter.paging.RecyclerDataSource
import net.gotev.recycleradapterdemo.R
import net.gotev.recycleradapterdemo.adapteritems.LabelItem
import net.gotev.recycleradapterdemo.datasource.MainActivityDataSource
import java.util.Random

class MainActivity : AppCompatActivity() {

    private val random by lazy {
        Random(System.currentTimeMillis())
    }

    private lateinit var recyclerAdapter: RecyclerAdapter

    private lateinit var pagingAdapter: PagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        recyclerAdapter = RecyclerAdapter()
//        recyclerAdapter.setEmptyItem(LabelItem(getString(R.string.empty_list)))

        pagingAdapter = PagingAdapter(
            activity = this,
            recyclerDataSource = MainActivityDataSource() as RecyclerDataSource<Any, AdapterItem<*>>,
            config = PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(true)
                .setPrefetchDistance(10)
                .build()
        ).apply {
            setEmptyItem(LabelItem(getString(R.string.empty_list)))
        }

        recycler_view.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = pagingAdapter
            //recyclerAdapter.enableDragDrop(this)
        }

        // add an item
//        recyclerAdapter.add(MyLeaveBehindItem("swipe to left to leave behind", "option"))
//
//        // add many items of two kinds
//        val items = (0..random.nextInt(200) + 50).map {
//            if (it % 2 == 0)
//                TitleSubtitleItem("Item $it")
//            else
//                TextWithToggleItem("Toggle $it")
//        }
//
//        recyclerAdapter.add(items)

        configureActions()
    }

    private fun configureActions() {
        remove_all_items_of_a_kind.setOnClickListener {
            //recyclerAdapter.removeAllItemsWithClass(TitleSubtitleItem::class.java)
        }

        remove_last_item_of_a_kind.setOnClickListener {
            //recyclerAdapter.removeLastItemWithClass(TextWithToggleItem::class.java)
        }

        remove_last_item_of_a_kind.setOnClickListener {
            //recyclerAdapter.removeLastItemWithClass(TextWithToggleItem::class.java)
        }

        remove_all.setOnClickListener {
            //pagingAdapter.clear()
        }

        add_item.setOnClickListener {
//            recyclerAdapter.add(
//                    TitleSubtitleItem("Item ${UUID.randomUUID()}"),
//                    position = 1
//            )
        }
    }

    private fun onSearch(query: String?) {
        //recyclerAdapter.filter(query)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    onSearch(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    onSearch(newText)
                    return false
                }
            })
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {

        R.id.sync_demo -> {
            SyncActivity.show(this)
            true
        }

        R.id.selection -> {
            SelectionActivity.show(this)
            true
        }

        R.id.selection_multi_groups -> {
            MasterSlaveGroupsActivity.show(this)
            true
        }

        R.id.api_integration -> {
            InfiniteScroll.show(this)
            true
        }

        R.id.carousels_plain -> {
            Carousels.show(this, withPool = false)
            true
        }

        R.id.carousels_pool -> {
            Carousels.show(this, withPool = true)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}
