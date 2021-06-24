package com.example.galleryapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ItemListFragment : Fragment() {
    private var orientationLand: Boolean = false
    private lateinit var recyclerView: RecyclerView
    private lateinit var mAdapter: MyItemRecyclerViewAdapter

    private fun getData(): MutableList<PhotoItem> {
        val photoMutableList = mutableListOf<PhotoItem>()
        for (i in 0 .. 6){
            photoMutableList.add(
                PhotoItem(
                    src = "src$i",
                    rates = 0F,
                    description = "Default description $i"
                )
            )
        }
        return photoMutableList
    }
    fun receiveIntentfromActivity(data: Intent?){
        val item = data?.getParcelableExtra<PhotoItem>("return item")
        val pos = data?.getIntExtra("pos", -1)
        mAdapter.setItem(item!!, pos!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)

        val activityContext = activity as Context
        recyclerView = view.findViewById(R.id.recycler_view)
        val cols = if (orientationLand) 3 else 2
        recyclerView.layoutManager = GridLayoutManager(activity, cols)
        mAdapter = MyItemRecyclerViewAdapter(activityContext, activity as MainActivity)
        recyclerView.adapter = mAdapter
        return view
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        orientationLand = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        val cols = if (orientationLand) 3 else 2
        recyclerView.layoutManager = GridLayoutManager(activity, cols)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            val savedList = savedInstanceState.getParcelableArrayList<PhotoItem>("list") as ArrayList<PhotoItem>
            mAdapter.setData(savedList)
        }
        else {
            mAdapter.setData(getData())
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("list", mAdapter.getData())
    }
    companion object {
        @JvmStatic
        fun newInstance() = ItemListFragment()
    }
}
