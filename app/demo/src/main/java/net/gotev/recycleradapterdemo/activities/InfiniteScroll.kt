package net.gotev.recycleradapterdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler_view.recycler_view
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.paging.PagingAdapter
import net.gotev.recycleradapter.paging.RecyclerDataSource
import net.gotev.recycleradapterdemo.R
import net.gotev.recycleradapterdemo.datasource.InfiniteScrollingDataSource

class InfiniteScroll : AppCompatActivity() {

    companion object {
        fun show(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, InfiniteScroll::class.java))
        }
    }

    private lateinit var pagingAdapter: PagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        title = getString(R.string.infinite_scrolling)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        pagingAdapter = PagingAdapter(
            activity = this,
            recyclerDataSource = InfiniteScrollingDataSource() as RecyclerDataSource<Any, AdapterItem<*>>,
            config = PagedList.Config.Builder()
                .setPageSize(20)
                .build()
        )

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = pagingAdapter
    }

}
