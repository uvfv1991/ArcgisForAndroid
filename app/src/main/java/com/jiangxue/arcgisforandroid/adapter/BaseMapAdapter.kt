package com.jiangxue.arcgisforandroid.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by LiuT on 2017/5/5.
 */
class BaseMapAdapter(context: Context?) : BaseAdapter<BaseMap?>(context) {
    fun setSelection(position: Int) {
        selectPosition = position
        val positionBaseMap: BaseMap = getData().get(position)
        for (baseMap in getData()) {
            baseMap.setChecked(positionBaseMap.equals(baseMap))
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

    val contentView: Int
        get() = R.layout.item_basemap_change

    fun onInitView(view: View, baseMap: BaseMap, position: Int) {
        view.setBackgroundResource(if (baseMap.isChecked()) R.color.colorAccent else R.color.colorTranslucent)
        val nameTextView: TextView = get(view, R.id.baseMap_name)
        val imageView: ImageView = get(view, R.id.baseMap_icon)
        nameTextView.setText(baseMap.getName())
        //Glide.with(context).load(baseMap.getIconPath()).placeholder(R.mipmap.ic_launcher).into(imageView);
        ClickHolder(view, baseMap, position)
    }

    private inner class ClickHolder(view: View, baseMap: BaseMap, position: Int) :
        View.OnClickListener {
        private val position: Int
        private val baseMap: BaseMap

        init {
            view.setOnClickListener(this)
            this.position = position
            this.baseMap = baseMap
        }

        override fun onClick(v: View) {
            if (!baseMap.isChecked()) {
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