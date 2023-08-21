package com.jiangxue.arcgisforandroid.widge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import butterknife.BindView

/**
 * Created by LiuT on 2017/5/4.
 *
 *
 * 底图切换的view
 */
class BaseMapChangeView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener,
    BaseMapAdapter.SelectCallback {
    @BindView(R.id.viewLayerList)
    var viewLayerList: ListView? = null

    @BindView(R.id.viewTitle)
    var viewTitle: TextView? = null

    @BindView(R.id.viewClose)
    var viewClose: ImageView? = null
    private val baseMapAdapter: BaseMapAdapter
    private var dismissListener: DismissListener? = null
    fun setDismissListener(dismissListener: DismissListener?) {
        this.dismissListener = dismissListener
    }

    private var callback: Callback? = null
    fun select(position: Int) {
        if (callback != null) {
            callback!!.select(baseMapAdapter.getItem(position))
        }
    }

    private var mapViewHelper: MapViewHelper? = null

    init {
        removeAllViews()
        val view = inflate(getContext(), R.layout.view_layer_list, null)
        addView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        ButterKnife.bind(this)
        viewClose!!.setOnClickListener(this)
        viewTitle!!.text = "底图切换"
        baseMapAdapter = BaseMapAdapter(context)
        viewLayerList!!.adapter = baseMapAdapter
        baseMapAdapter.setSelectCallback(this)
    }

    fun setMapViewHelper(mapViewHelper: MapViewHelper?) {
        this.mapViewHelper = mapViewHelper
    }

    interface Callback {
        fun select(baseMap: BaseMap?)
    }

    fun showList(list: List<BaseMap?>?, callback: Callback?) {
        this.callback = callback
        baseMapAdapter.setData(list)
    }

    fun setSelect(position: Int) {
        val list: List<BaseMap> = baseMapAdapter.getData()
        if (list != null && list.size > position) {
            baseMapAdapter.setSelection(position)
            if (mapViewHelper != null) {
                if (callback != null) {
                    callback!!.select(list[position])
                }
            }
        }
    }

    val selectPosition: Int
        get() = baseMapAdapter.getSelectPosition()

    override fun onClick(v: View) {
        when (v.id) {
            R.id.viewClose -> if (dismissListener != null) {
                dismissListener.dismiss()
            }
        }
    }
}