package net.gotev.recycleradapterdemo.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_recycler_view.recycler_view
import kotlinx.android.synthetic.main.activity_recycler_view.swipeRefresh
import net.gotev.recycleradapter.AdapterItem
import net.gotev.recycleradapter.paging.LoadingState
import net.gotev.recycleradapter.paging.PagingAdapter
import net.gotev.recycleradapter.paging.RecyclerDataSource
import net.gotev.recycleradapterdemo.App
import net.gotev.recycleradapterdemo.R
import net.gotev.recycleradapterdemo.adapteritems.LabelItem
import net.gotev.recycleradapterdemo.network.api.StarWarsPeopleDataSource

class LoadFromNetwork : AppCompatActivity() {

    companion object {
        fun show(activity: AppCompatActivity) {
            activity.startActivity(Intent(activity, LoadFromNetwork::class.java))
        }
    }

    private lateinit var pagingAdapter: PagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        title = getString(R.string.network_call)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        pagingAdapter = PagingAdapter(
            activity = this,
            recyclerDataSource = StarWarsPeopleDataSource(App.starWarsClient) as RecyclerDataSource<Any, AdapterItem<*>>,
            config = PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(20)
                .setPrefetchDistance(10)
                .setMaxSize(50)
                .build(),
            emptyItem = LabelItem(getString(R.string.empty_list)),
            showEmptyItemOnStartup = true,
            errorItem = LabelItem("Oh no 😿, it seems that something gone wrong!")
        )

        recycler_view.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.adapter = pagingAdapter

        pagingAdapter.getState().observe(this, Observer {
            swipeRefresh.isRefreshing = (it == LoadingState.LOADING)
        })

        swipeRefresh.setOnRefreshListener {
            pagingAdapter.reload()
        }

    }

}
