package com.jiangxue. arcgisforandroid.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jiangxue.arcgisforandroid.R
import com.jiangxue.arcgisforandroid.adapter.BaseAdapter
import com.jiangxue.arcgisforandroid.data.xml.properties.BaseMap


class BaseMapAdapter(context: Context?) : BaseAdapter<BaseMap?>(context) {
    fun setSelection(position: Int) {
        selectPosition = position
        val positionBaseMap: BaseMap? = data?.get(position)
        for (baseMap in data!!) {
            baseMap?.isChecked ?: (positionBaseMap?.equals(baseMap))
        }
        notifyDataSetChanged()
    }

    interface SelectCallback {
        fun select(position: Int)
    }

    private var selectCallback: SelectCallback? = null
    fun setSelectCallback(selectCallback: SelectCallback?) {
        this.selectCallback = selectCallback
    }

    override val contentView: Int
        get() = R.layout.item_basemap_change

    override fun onInitView(view: View?, t: BaseMap?, position: Int) {
        view?.setBackgroundResource(R.color.colorAccent)
        val nameTextView: TextView = get(view!!, R.id.baseMap_name)!!
        val imageView: ImageView = get(view, R.id.baseMap_icon)!!
        nameTextView.setText(t?.name)
        //Glide.with(context).load(baseMap.getIconPath()).placeholder(R.mipmap.ic_launcher).into(imageView);
        ClickHolder(view, t, position)
    }



    private inner class ClickHolder(view: View, baseMap: BaseMap?, position: Int) :
        View.OnClickListener {
        private val position: Int
        private val baseMap: BaseMap

        init {
            view.setOnClickListener(this)
            this.position = position
            this.baseMap = baseMap!!
        }

        override fun onClick(v: View) {
            if (!baseMap.isChecked) {
                if (selectCallback != null) {
                    selectCallback!!.select(position)
                    setSelection(position)
                }
            }
        }
    }

    var selectPosition = -1
        private set
}