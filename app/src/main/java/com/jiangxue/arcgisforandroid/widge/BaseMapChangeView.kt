package com.jiangxue.arcgisforandroid.widge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.jiangxue.arcgisforandroid.R
import com.jiangxue.arcgisforandroid.adapter.BaseMapAdapter
import com.jiangxue.arcgisforandroid.data.xml.properties.BaseMap
import com.jiangxue.arcgisforandroid.mapview.MapViewHelper
import kotlinx.android.synthetic.main.view_layer_list.view.viewLayerList
import kotlinx.android.synthetic.main.view_title.view.viewClose
import kotlinx.android.synthetic.main.view_title.view.viewTitle

/**
 * 底图切换的view
 */
public class BaseMapChangeView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener,
    BaseMapAdapter.SelectCallback {

    private val baseMapAdapter: BaseMapAdapter
    private var dismissListener: ActionView.DismissListener? = null
    fun setDismissListener(dismissListener: ActionView.DismissListener?) {
        this.dismissListener = dismissListener
    }

    private var callback: Callback? = null
    override fun select(position: Int) {
        if (callback != null) {
            callback!!.select(baseMapAdapter.getItem(position))
        }
    }

    private var mapViewHelper: MapViewHelper? = null

    init {
        removeAllViews()
        val view = inflate(getContext(), R.layout.view_layer_list, null)
        addView(view, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))

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


    fun setSelect(position: Int) {
        val list: List<BaseMap?>? = baseMapAdapter.data
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
        get() = baseMapAdapter.selectPosition
    override fun onClick(v: View) {
        when (v.id) {
            R.id.viewClose -> if (dismissListener != null) {
                dismissListener!!.dismiss()
            }
        }
    }

    fun show(list: List<BaseMap>, callback: Callback?) {
        this.callback = callback
        baseMapAdapter.setData(list as ArrayList<BaseMap?>)
    }
}