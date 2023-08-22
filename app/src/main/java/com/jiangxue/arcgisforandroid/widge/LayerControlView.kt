package com.jiangxue.arcgisforandroid.widge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.ArrayUtils
import com.jiangxue.arcgisforandr.LayerControlAdapter
import com.jiangxue.arcgisforandroid.R
import com.jiangxue.arcgisforandroid.data.LayerCategoryBean
import com.jiangxue.arcgisforandroid.mapview.MapViewHelper
import kotlinx.android.synthetic.main.view_layer_control.view.expandable_list_view
import kotlinx.android.synthetic.main.view_title.view.viewClose
import kotlinx.android.synthetic.main.view_title.view.viewTitle

/**
 * Created by Admin
 */
class LayerControlView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener{

    private var layerControlAdapter: LayerControlAdapter? = null
    private var mapViewHelper: MapViewHelper? = null
    private var isInted = false
    fun setData(mapViewHelper: MapViewHelper?, layerCategoryBeanList: List<LayerCategoryBean>) {
        this.mapViewHelper = mapViewHelper
        if (layerControlAdapter == null) {
            layerControlAdapter = LayerControlAdapter(mapViewHelper)
            expandable_list_view.setAdapter(layerControlAdapter)
        }
        layerControlAdapter!!.setData(layerCategoryBeanList)
        if (!isInted) {
            if (!layerCategoryBeanList.isEmpty()) {
                for (x in layerCategoryBeanList.indices) {
                    if (x == 0) {
                        expandable_list_view.expandGroup(x, true)
                    } else {
                        expandable_list_view.collapseGroup(x)
                    }
                }
            }
            isInted = true
        }
    }

    private var dismissListener: ActionView.DismissListener? = null
    fun setDismissListener(dismissListener: ActionView.DismissListener?) {
        this.dismissListener = dismissListener
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.viewClose -> if (dismissListener != null) {
                dismissListener!!.dismiss()
            }
        }
    }



    init {
        removeAllViews()
        val view = View.inflate(context, R.layout.view_layer_control, null)
        addView(
            view,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
        viewTitle.setText("项目图层")
        viewClose.setOnClickListener(this)

        expandable_list_view.setGroupIndicator(null) //去掉箭头
    }



}