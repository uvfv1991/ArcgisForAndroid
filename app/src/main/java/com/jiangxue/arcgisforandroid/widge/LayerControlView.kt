package com.jiangxue.arcgisforandroid.widge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import butterknife.BindView

/**
 * Created by Jinyu Zhang on 2017/5/5.
 */
class LayerControlView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener, LayerControlListener {
    @BindView(R.id.expandable_list_view)
    var expandableListView: ExpandableListView? = null

    @BindView(R.id.viewTitle)
    var viewTitle: TextView? = null

    @BindView(R.id.viewClose)
    var viewClose: ImageView? = null

    @BindView(R.id.color_set_view)
    var colorSetView: ColorSetView? = null
    private var layerControlAdapter: LayerControlAdapter? = null
    private var mapViewHelper: MapViewHelper? = null
    private var isInted = false
    fun setData(mapViewHelper: MapViewHelper?, layerCategoryBeanList: List<LayerCategoryBean?>) {
        this.mapViewHelper = mapViewHelper
        if (layerControlAdapter == null) {
            layerControlAdapter = LayerControlAdapter(mapViewHelper as MapViewObserver?)
            layerControlAdapter.setLayerControlListener(this)
            expandableListView.setAdapter(layerControlAdapter)
        }
        layerControlAdapter.setData(layerCategoryBeanList)
        if (!isInted) {
            if (!Check.isEmpty(layerCategoryBeanList)) {
                for (x in layerCategoryBeanList.indices) {
                    if (x == 0) {
                        expandableListView.expandGroup(x, true)
                    } else {
                        expandableListView.collapseGroup(x)
                    }
                }
            }
            isInted = true
        }
    }

    private var dismissListener: DismissListener? = null
    fun setDismissListener(dismissListener: DismissListener?) {
        this.dismissListener = dismissListener
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.viewClose -> if (dismissListener != null) {
                dismissListener.dismiss()
            }
        }
    }

    private var shp: Shp? = null
    private var featureLayer: FeatureLayer? = null
    private var colorInit = 0
    fun showColorSetView(featureLayer: FeatureLayer?, shp: Shp) {
        this.featureLayer = featureLayer
        this.shp = shp
        colorInit = shp.getLayerColor()
        //        colorSetView.setColor(shp.getLayerColor());
        colorSetView.setColor(shp.getLayerColor(), shp.getLineColor())
        colorSetView.setFrameWidth(shp.getLineWidth())
        colorSetView.setVisibility(View.VISIBLE)
    }

    private fun updateLayerColor(shp: Shp?) {
//        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 1);
//        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, shp.getLayerColor(), lineSymbol);
//
//        fillSymbol.getOutline().setColor(shp.getLineColor());
//        fillSymbol.getOutline().setWidth(shp.getLineWidth());
//        featureLayer.setRenderer(new SimpleRenderer(fillSymbol));
//        featureLayer.setVisible(true);
    }

    fun setLayerColor(fullColor: Int, frameColor: Int, width: Int, isSure: Boolean) {
        shp.setLayerColor(ColorUtils.getColorToRGBWithAlpha(fullColor))
        shp.setLineColor(ColorUtils.getColorToRGBWithAlpha(frameColor))
        shp.setLineWidth(width.toString())
        if (isSure) {
            colorSetView.setVisibility(View.GONE)
        }
        updateLayerColor(shp)
    }

    fun cancel() {
        shp.setLayerColor(ColorUtils.getColorToRGBWithAlpha(colorInit))
        colorSetView.setVisibility(View.GONE)
        updateLayerColor(shp)
    }

    fun setDefault() {
        colorSetView.setVisibility(View.GONE)
    }

    fun colorSet(shp: Shp) {
        showColorSetView(mapViewHelper.getShpLayer(shp), shp)
        if (layerControlListener != null) {
            layerControlListener.colorSet(shp)
        }
    }

    private var layerControlListener: LayerControlListener? = null

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
        ButterKnife.bind(this)
        viewTitle.setText("项目图层")
        viewClose!!.setOnClickListener(this)
        colorSetView.setColorSetResultListener(this)
        expandableListView.setGroupIndicator(null) //去掉箭头
    }

    fun setLayerControlListener(layerControlListener: LayerControlListener?) {
        this.layerControlListener = layerControlListener
    }

    fun wholePicture(shp: Shp) {
        mapViewHelper.layerWholePicture(shp.getId())
        if (layerControlListener != null) {
            layerControlListener.wholePicture(shp)
        }
    }
}