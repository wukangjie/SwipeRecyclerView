package com.wukangjie.swipe

import android.annotation.SuppressLint
import android.app.Service
import android.os.Bundle
import android.os.Vibrator
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.wukangjie.swipe.MainAdapter.ViewHolder
import java.util.*
import kotlin.collections.ArrayList
import com.wukangjie.swipe.MainAdapter.OnLongClick as OnLongClick1

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var data: ArrayList<Menu>

    private lateinit var name: Array<String>

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        name = resources.getStringArray(R.array.food)
        val ar = resources.obtainTypedArray(R.array.icon)

        data = ArrayList()

        val size = name.size
        for (i in 0 until size) {
            data.add(Menu(ar.getResourceId(i, 0), name[i]))
        }

        ar.recycle()

        adapter = MainAdapter(this, data)

        adapter.onLongClick = object : OnLongClick1 {
            @SuppressLint("ShowToast")
            override fun onClick(position: Int, viewHolder: ViewHolder): Boolean {
                if (TextUtils.equals(data[position].menuName, "开心果")) {
                    Toast.makeText(baseContext, "开心果是不可以拖动的哦", Toast.LENGTH_SHORT)
                    return true
                }

                itemTouchHelper.startDrag(viewHolder)

                //获取系统震动服务
                val vib = getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                //震动70毫秒

                vib.vibrate(70)
                return false
            }


        }

        recyclerView = findViewById(R.id.main_recycler)
        recyclerView.layoutManager = StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun canDropOver(
            recyclerView: RecyclerView,
            current: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return if (TextUtils.equals(
                    data.get(data.size - 1).menuName,
                    "开心果"
                ) && (current.adapterPosition == data.size - 1 || target.adapterPosition == data.size - 1)
            ) {
                false
            } else super.canDropOver(recyclerView, current, target)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            if (TextUtils.equals(
                    data.get(data.size - 1).menuName,
                    "开心果"
                ) && (viewHolder.adapterPosition == data.size - 1 || target.adapterPosition == data.size - 1)
            ) {
                return false
            } else {
                //得到当拖拽的viewHolder的Position
                val fromPosition = viewHolder.adapterPosition
                //拿到当前拖拽到的item的viewHolder
                val toPosition = target.adapterPosition
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(data, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(data, i, i - 1)
                    }
                }
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {

        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            recyclerView.invalidate()
        }

    })
}
