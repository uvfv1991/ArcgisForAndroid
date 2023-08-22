package com.jiangxue.arcgisforandroid.adapter

import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter


/**
 * Created by zhangjinyu on 2016/4/5.
 */
abstract class BaseAdapter<T>(protected var context: Context?) : BaseAdapter() {
    private var list: ArrayList<T> = ArrayList()

    /**
     * 获取列表的全部数据
     *
     * @return
     */
    val data: ArrayList<T>?
        get() = list

    /**
     * 设置list数据
     *
     * @param list
     */
    fun setData(list: ArrayList<T>) {
       this.list = list
        if (list != null) {
//            Logger.d(new Gson().toJson(list));
        }
        notifyDataSetChanged()
    }

    /**
     * 清空列表
     */
    fun clear() {
        list!!.clear()
        notifyDataSetChanged()
    }

    /**
     * 增加整个list数据
     *
     * @param list
     */
    fun addData(list: List<T>?) {
        if (list != null) {
            this.list!!.addAll(list)
            notifyDataSetChanged()
        }
    }

    fun remove(t: T) {
        if (list != null) {
            list!!.remove(t)
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): T? {
        return if (list == null || list!!.isEmpty()) {
            null
        } else list!![position]
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        if (null == convertView) {
            convertView = inflate(contentView)
        }
        onInitView(convertView, data!![position], position)
        return convertView
    }

    /**
     * 加载布局
     */
    private fun inflate(layoutResID: Int): View {
        val layoutInflater = context
            ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return layoutInflater.inflate(layoutResID, null)
    }

    abstract val contentView: Int
    abstract fun onInitView(view: View?, t: T, position: Int)

    /**
     * @param view converView
     * @param id   控件的id
     * @return 返回
     */
    protected operator fun <E : View?> get(view: View, id: Int): E? {
        var viewHolder = view.tag as SparseArray<*>
        if (null == viewHolder) {
            viewHolder = SparseArray<Any?>()
            view.tag = viewHolder
        }
        var childView: View? = viewHolder[id] as E
        if (null == childView) {
            childView = view.findViewById(id)
            viewHolder.put(id, childView as Nothing?)
        }
        return childView as E?
    }
}