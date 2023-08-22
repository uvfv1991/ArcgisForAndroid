package com.jiangxue.arcgisforandroid

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import com.blankj.utilcode.util.ArrayUtils
import com.blankj.utilcode.util.ToastUtils
import com.esri.arcgisruntime.concurrent.ListenableFuture
import com.esri.arcgisruntime.data.FeatureQueryResult
import com.esri.arcgisruntime.data.QueryParameters
import com.esri.arcgisruntime.geometry.Geometry
import com.esri.arcgisruntime.geometry.GeometryEngine
import com.esri.arcgisruntime.geometry.Polygon
import com.esri.arcgisruntime.geometry.SpatialReference
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.mapping.view.SketchCreationMode
import com.esri.arcgisruntime.mapping.view.SketchEditor
import com.esri.arcgisruntime.symbology.TextSymbol
import com.example.perfectplayer.event.MessageEvent
import com.jiangxue.arcgisforandroid.base.BaseActivity
import com.jiangxue.arcgisforandroid.base.NoViewModel
import com.jiangxue.arcgisforandroid.data.xml.properties.BaseMap
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.Shp
import com.jiangxue.arcgisforandroid.databinding.MapArcgisBinding
import com.jiangxue.arcgisforandroid.helper.XmlDataHelper
import com.jiangxue.arcgisforandroid.mapview.MapViewHelper
import com.jiangxue.arcgisforandroid.widge.ActionView
import com.jiangxue.arcgisforandroid.widge.BaseMapChangeView
import com.jiangxue.arcgisforandroid.widge.BaseMapChangeView.Callback
import com.jiangxue.arcgisforandroid.widge.LayerControlView
import kotlinx.android.synthetic.main.map_arcgis.action_view
import kotlinx.android.synthetic.main.map_arcgis.btn_menu
import kotlinx.android.synthetic.main.map_arcgis.coordinates
import kotlinx.android.synthetic.main.map_arcgis.cotrolllayer
import kotlinx.android.synthetic.main.map_arcgis.drawLayout
import kotlinx.android.synthetic.main.map_arcgis.fab_sumbmit
import kotlinx.android.synthetic.main.map_arcgis.map_view
import kotlinx.android.synthetic.main.map_arcgis.redo
import kotlinx.android.synthetic.main.map_arcgis.startDraw
import kotlinx.android.synthetic.main.map_arcgis.stopDraw
import kotlinx.android.synthetic.main.map_arcgis.undo
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.toast
import java.text.DecimalFormat


/**
 * Created by JiangXue on 2022/5/12.
 * 影像单元绘制
 */
class MapActivity : BaseActivity<NoViewModel, MapArcgisBinding>(), ActionView.DismissListener,
    com.jiangxue.arcgisforandroid.helper.XmlDataHelper.BasicDataPrepareCallback,OnClickListener{
    private var sketchEditor: SketchEditor? = null

    //默认是面
    private val sketchCreationMode: SketchCreationMode = SketchCreationMode.POLYGON
    private var  map_viewHelper: MapViewHelper? = null
    private lateinit var xmlDataHelper: com.jiangxue.arcgisforandroid.helper.XmlDataHelper
    private var layerControlView: LayerControlView? = null
    private var windowManager: WindowManager? = null
    private var unitnum //临时单元号
            : String? = null
    private var status = false
    private var isjoin = false
    private var sunitBuilder: StringBuilder? = null
    private var map: ArcGISMap? = null
    private var binding: MapArcgisBinding? = null
    var geometry: Geometry? = null
    var allPolygon: MutableList<Polygon?>? = null



    private fun initViewEvent() {
        //开始绘制
         startDraw.setOnClickListener(View.OnClickListener {
            sketchEditor?.start(sketchCreationMode)
            toast("开始绘制")
             drawLayout.setVisibility(View.VISIBLE)
        })
        //取消当前绘制
         stopDraw.setOnClickListener(View.OnClickListener {
            sketchEditor?.stop()
             toast("取消绘制")
             drawLayout.setVisibility(View.INVISIBLE)
        })

        //绘图完成并提交
         fab_sumbmit.setOnClickListener(View.OnClickListener {
            if (sketchEditor?.isSketchValid()!!) {
                geometry = sketchEditor?.getGeometry()

            } else {

            }
        })


        //图层控制
         cotrolllayer.setOnClickListener(View.OnClickListener {
            layerControl()
             toast("图层控制")
        })
        //监听菜单按键
         btn_menu.setOnMenuToggleListener{
             if (!it) {
                 drawLayout.setVisibility(View.INVISIBLE)
                 //如果有未保存的绘图则直接删除
                 geometry = null
                 sketchEditor?.stop()
             }
         }

        //取消一个点
         undo.setOnClickListener { view -> sketchEditor?.undo() }
        //前进一个点
         redo.setOnClickListener { view -> sketchEditor?.redo() }
    }

    //判断是否与原地块中单元相交
    private fun checkShpUnit( map_view: MapView, gmt: Geometry): Boolean {
        isjoin = false
        val query = QueryParameters()
        query.setGeometry(gmt)
        for (layer in  map_view.getMap().getOperationalLayers()) {
            val featureLayer: FeatureLayer = layer as FeatureLayer
            val featureQueryResultFuture: ListenableFuture<FeatureQueryResult> = featureLayer
                .selectFeaturesAsync(query, FeatureLayer.SelectionMode.NEW)

            // add done loading listener to fire when the selection returns
            featureQueryResultFuture.addDoneListener {

                // Get the selected features
                val featureQueryResultFuture1: ListenableFuture<FeatureQueryResult> =
                    featureLayer.getSelectedFeaturesAsync()
                featureQueryResultFuture1.addDoneListener {
                    try {
                        val layerFeatures: FeatureQueryResult = featureQueryResultFuture1.get()
                        for (feature in layerFeatures) {
                            if (!GeometryEngine.disjoint(gmt, feature.getGeometry())) {
                                isjoin = true
                                Log.e("test", "发现相交项")
                                return@addDoneListener
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("test", "Select feature failed: " + e.message)
                    }
                }
            }
        }
        return isjoin
    }

    //判断图形是否与已添加临时单元图形相交
    fun checkUnitPolygon(geometry: Geometry?): Boolean {
        for (i in allPolygon!!.indices) {
            val gmt: Geometry = GeometryEngine.project(geometry, SpatialReference.create(4531))
            if (!GeometryEngine.disjoint(allPolygon!![i], gmt)) {
                return true
            }
        }
        return false
    }


    //图层控制
    fun layerControl() {
        if (null == layerControlView) {
            layerControlView = LayerControlView(this)
            layerControlView!!.setDismissListener(this)
        }
        layerControlView!!.setData(map_viewHelper, xmlDataHelper.layerCategoryBeanList!!)
        action_view.setDeafult();
        action_view.showView(layerControlView);

    }


    fun readProperties() {
        xmlDataHelper?.prepareBasicData(this)
    }
    @Subscribe
    fun onEvent(event: MessageEvent) {
        when (event.getType()) {
        }
    }
    override fun onStart() {
        super.onStart()
    }

     override fun onResume() {
        super.onResume()
          map_view.resume()
    }

    override fun onPause() {
        super.onPause()
          map_view.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
          map_view.dispose()
    }

    override fun layoutId(): Int {
        return R.layout.map_arcgis
    }

    @SuppressLint("SuspiciousIndentation")
    override fun initView(savedInstanceState: Bundle?) {
        map = ArcGISMap()
        map_view.setMap(map)
        sketchEditor = SketchEditor()
        map_view.setSketchEditor(sketchEditor)
        map_view.setAttributionTextVisible(false)
        initViewEvent()
        windowManager = getWindowManager()
        xmlDataHelper = XmlDataHelper()
         map_viewHelper = MapViewHelper(map_view)
        //已绘画完成的单元
        allPolygon = ArrayList<Polygon?>()

        //设置点击事件
        map_view.setOnTouchListener(object :
            DefaultMapViewOnTouchListener(this,  map_view) {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (!xmlDataHelper!!.isDataIsPrepared) {
                    return false
                }
                val screenPoint = Point(e.getX().toInt(), e.getY().toInt())
                val point1: com.esri.arcgisruntime.geometry.Point? = mMapView.screenToLocation(screenPoint)
                val format = DecimalFormat("0.000000")
                val wgsPoint: Point =
                    GeometryEngine.project(point1, SpatialReference.create(4326)) as Point
                if (wgsPoint != null) {
                     coordinates.setText(
                        "x : " + format.format(wgsPoint.x) + "   y : " + format.format(
                            wgsPoint.y
                        )
                    )
                }
                return super.onSingleTapConfirmed(e)
            }
        })
         map_viewHelper!!.setXmlDataHelper(xmlDataHelper)

        readProperties()
    }


    var baseMapChangeView: BaseMapChangeView? = null
    override fun initData() {
        val baseMaps = xmlDataHelper.properties!!.baseMap
        if (!baseMaps.isEmpty()) {
            if (baseMapChangeView == null) {
                baseMapChangeView = BaseMapChangeView(this)
                baseMapChangeView!!.setMapViewHelper(map_viewHelper)
                baseMapChangeView!!.setDismissListener(this)

                baseMapChangeView!!.show(xmlDataHelper.baseMap!!, object : Callback {
                    override fun select(baseMap: BaseMap?) {
                        map_viewHelper?.setBaseMap(baseMap!!)
                    }

                })
                baseMapChangeView!!.setSelect(0)
            }
            map_viewHelper?.setShps(xmlDataHelper!!.allShps as List<Shp>)

        }

    }

    override fun dismiss() {
        action_view.dismiss()
    }

    override fun dismissAndClear() {

    }

    override fun dismissCallOut(isClose: Boolean) {
    }

    override fun prepareSuccess() {
            initData()
    }


    override fun prepareError(errorMsg: String?) {
        Log.e("prepareError: ",errorMsg.toString() )
    }





    //图层获取
    private fun getGraphicsOverlay( map_view: MapView): GraphicsOverlay {
        val num: Int =  map_view.getGraphicsOverlays().size
        return if (num < 1) {
            val graphicsOverlay = GraphicsOverlay()
            //add the overlay to the map view
             map_view.getGraphicsOverlays().add(graphicsOverlay)
            graphicsOverlay
        } else {
             map_view.getGraphicsOverlays().get(0)
        }
    }

    //create text symbols
    private fun addText(graphicOverlay: GraphicsOverlay, point: Point, Text: String) {
        val bassRockSymbol = TextSymbol(
            10F, Text, Color.rgb(0, 0, 230),
            TextSymbol.HorizontalAlignment.CENTER,
            TextSymbol.VerticalAlignment.MIDDLE
        )
       // val bassRockGraphic = Graphic(point, bassRockSymbol)
        //add the text to the graphics overlay
        //add the text to the graphics overlay
        //graphicOverlay.graphics.add(bassRockGraphic)
    }


    companion object {
        private const val DOUBLE_CLICK_TIME = 0.0
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.ll_layer_control -> layerControl()
            R.id.tv_back -> finish()
            R.id.tv_back -> finish()
        }
    }


}