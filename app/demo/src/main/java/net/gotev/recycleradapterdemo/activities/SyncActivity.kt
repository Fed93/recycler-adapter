package net.gotev.recycleradapterdemo.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_sync.*
import net.gotev.recycleradapter.paging.PagingAdapter
import net.gotev.recycleradapterdemo.R
import net.gotev.recycleradapterdemo.adapteritems.LabelItem
import net.gotev.recycleradapterdemo.datasource.SyncDataSourceA
import net.gotev.recycleradapterdemo.datasource.SyncDataSourceB
import net.gotev.recycleradapterdemo.datasource.SyncDataSourceC
import net.gotev.recycleradapterdemo.datasource.SyncDataSourceShuffle
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit


class SyncActivity : AppCompatActivity() {

    companion object {
        fun show(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, SyncActivity::class.java))
        }
    }

    private lateinit var recyclerAdapter: PagingAdapter
    private var executor = ScheduledThreadPoolExecutor(1)
    private var scheduledOperation: ScheduledFuture<*>? = null

    private val config = PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(true)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        title = getString(R.string.sync_with_items)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        recyclerAdapter = PagingAdapter(
            activity = this,
            config = config,
            emptyItem = LabelItem(getString(R.string.empty_list)),
            showEmptyItemOnStartup = true
        )

//        recyclerAdapter = RecyclerAdapter().apply {
//            setEmptyItem(LabelItem(getString(R.string.empty_list)))
//            lockScrollingWhileInserting(linearLayoutManager)
//        }

        recycler_view.apply {
            // normal setup
            layoutManager = linearLayoutManager
            adapter = recyclerAdapter
        }

        syncA.setOnClickListener {
            recyclerAdapter.setDataSource(SyncDataSourceA(), config)
        }

        syncB.setOnClickListener {
            recyclerAdapter.setDataSource(SyncDataSourceB(), config)
        }

        syncC.setOnClickListener {
            recyclerAdapter.setDataSource(SyncDataSourceC(), config)
        }

        empty.setOnClickListener {
            recyclerAdapter.clear()
        }

        shuffle.setOnClickListener {
            scheduledOperation = if (scheduledOperation == null) {
                shuffle.text = getString(R.string.button_shuffle_stop)
                executor.scheduleAtFixedRate({
                    runOnUiThread {
                        recyclerAdapter.setDataSource(SyncDataSourceShuffle(), config)
                    }
                }, 1, 100, TimeUnit.MILLISECONDS)
            } else {
                shuffle.text = getString(R.string.button_shuffle_start)
                scheduledOperation?.cancel(true)
                null
            }
        }
    }

    override fun onPause() {
        super.onPause()
        scheduledOperation?.cancel(true)
        scheduledOperation = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sort_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.sort_ascending -> {
            //recyclerAdapter.sort(ascending = true)
            true
        }

        R.id.sort_descending -> {
            //recyclerAdapter.sort(false)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

}
