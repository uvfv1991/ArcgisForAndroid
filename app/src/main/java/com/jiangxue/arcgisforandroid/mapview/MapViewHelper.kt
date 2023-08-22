package com.jiangxue.arcgisforandroid.mapview

import android.graphics.Color
import android.util.Log
import com.esri.arcgisruntime.arcgisservices.LabelDefinition
import com.esri.arcgisruntime.data.Geodatabase
import com.esri.arcgisruntime.data.GeodatabaseFeatureTable
import com.esri.arcgisruntime.data.ShapefileFeatureTable
import com.esri.arcgisruntime.data.TileCache
import com.esri.arcgisruntime.layers.ArcGISTiledLayer
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.layers.Layer
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.LayerList
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleRenderer
import com.jiangxue.arcgisforandr.LayerControlAdapter
import com.jiangxue.arcgisforandroid.data.xml.properties.BaseMap
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.Shp
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.ShpType
import com.jiangxue.arcgisforandroid.helper.XmlDataHelper
import com.jiangxue.arcgisforandroid.widge.MapViewObserver
import io.reactivex.disposables.Disposable

/**
 * 地图显示
 */
class MapViewHelper(mapView: MapView?) : MapViewObserver(mapView) {
    private val layers: MutableList<Layer>? = ArrayList()
    private var bcdyGraphicsLayer //XD项目
            : GraphicsOverlay? = null
    private var fxjGraphicsLayer //富新街项目
            : GraphicsOverlay? = null
    private var yhskGraphicsLayer //延河水库项目
            : GraphicsOverlay? = null

    override fun layerControl(actionGuide: LayerControlAdapter.ActionGuide?) {
        // 根据选择的图层和当前层级显示各种布局(xd项目，富新街，延河水库)
        val layer: Layer? = getFeatureLayerByName(actionGuide?.layerName.toString())
        when (actionGuide?.guide) {
            0 -> {}
            1 -> if (layer != null) {
                layer.isVisible = true
                val centerPnt = layer.fullExtent.center
                mapView!!.setViewpointCenterAsync(centerPnt, 55000000.0)
            }

            2 -> if (layer != null) {
                layer.isVisible = false
            }
        }
        val operationalLayers: LayerList = mapView!!.map.operationalLayers
        var isBCDYShow = false
        var isFXJShow = false
        var isYHSKShow = false

        if (isBCDYShow) {
            showBUCHANDANYUANPaletteGraphics()
        } else {
            goneBUCHANDANYUANPaletteGraphics()
        }
        if (isFXJShow) {
            showFUXINJIEPaletteGraphics()
        } else {
            goneFUXINJIEPaletteGraphics()
        }
        if (isYHSKShow) {
            showYHSKPaletteGraphics()
        } else {
            goneYHSKPaletteGraphics()
        }
    }

    val layersLength: Int
        get() {
            val layers: LayerList = mapView!!.map.operationalLayers
            return if (mapView != null && this.layers != null) {
                this.layers.size
            } else 0
        }
    var isLoaded = false
        private set

    fun setBaseMap(baseMap: BaseMap) {
        val mapType = baseMap.mapType
        when (baseMap.mapType) {
            0 -> {}
            1 -> {//离线数据
                val placeholder = baseMap.placeholder
                val mainTileCache = TileCache(baseMap.placeholder)
                val mainArcGISTiledLayer = ArcGISTiledLayer(mainTileCache)
                val mainBasemap = Basemap(mainArcGISTiledLayer)
                val pMap = ArcGISMap(mainBasemap)
                mapView!!.map = pMap
            }

            2 -> {}
        }
    }

    /**
     * 增加显示图层
     *
     * @param featureLayer
     */
    @Synchronized
    fun addFeatureLayer(featureLayer: FeatureLayer) {
        layers!!.add(featureLayer)
        addLayer(featureLayer, layersLength)
    }

    /**
     * Helper method to get the named feature layer from the map's operational layers.
     *
     * @param layerName as a String
     * @return the named feature layer, or null, if not found or if named layer is not a feature layer
     */
    private fun getFeatureLayerByName(layerName: String): FeatureLayer? {
        val operationalLayers: LayerList = mapView!!.map.operationalLayers
        for (layer in operationalLayers) {
            if (layer is FeatureLayer && layer.name == layerName) {
                return layer as FeatureLayer
            }
        }
        return null
    }

    fun getShpLayer(shp: Shp): FeatureLayer? {
        for (layer in mapView!!.map.operationalLayers) {
            if (layer.name == null) {
                return null
            }
            if (layer.name === shp.name) {
                return layer as FeatureLayer
            }
        }
        return null
    }

    fun getTpkLayer(shp: Shp): Layer? {
        for (layer in mapView!!.map.operationalLayers) {
            if (layer.name == null) {
                return null
            }
            if (layer.name == shp.name) {
                return layer
            }
        }
        return null
    }

    val visiableShps: List<Any>?
        get() {
            if (allShps == null) {
                return null
            }
            val shpList: MutableList<Shp> = ArrayList<Shp>()
            for (layer in mapView!!.map.operationalLayers) {
                if (layer.isVisible) {
                    for (shp in allShps!!) {
                        shpList.add(shp)
                        if (layer.name == shp.name) {
                            shpList.add(shp)
                        }
                    }
                }
            }
            return shpList
        }

    private fun addLayer(layer: Layer, index: Int) {
        if (mapView != null) {
            if (index == -1) {
                mapView!!.map.operationalLayers.add(layer)
            } else {
                mapView!!.map.operationalLayers.add(index, layer)
            }
        }
    }

    private var allShps: List<Shp>? = null
    fun setShps(shps: List<Shp>) {
        //延河水库项目
        if (bcdyGraphicsLayer == null) {
            // 补偿单元信息图层（xd项目）
            bcdyGraphicsLayer = GraphicsOverlay()
            bcdyGraphicsLayer!!.setVisible(false)
        }
        if (fxjGraphicsLayer == null) {
            // 富新街信息图层
            fxjGraphicsLayer = GraphicsOverlay()
            fxjGraphicsLayer!!.setVisible(false)
        }
        if (yhskGraphicsLayer == null) {
            // 富新街信息图层
            yhskGraphicsLayer = GraphicsOverlay()
            yhskGraphicsLayer!!.setVisible(false)
        }
        isLoaded = true
        allShps = shps
        for (shp in shps) {
            try {
                when (shp.shpType) {
                    ShpType.GEO_DATABASE -> {
                        val geodatabase = Geodatabase(shp.getShpPath())
                        geodatabase.loadAsync()
                        val geodatabaseTables: List<GeodatabaseFeatureTable> =
                            geodatabase.getGeodatabaseFeatureTables()
                        for (geodatabaseFeatureTable in geodatabaseTables) {
                            val databaseFeatureLayer = FeatureLayer(geodatabaseFeatureTable)
                            shp.id=databaseFeatureLayer.getId().toLong()
                            databaseFeatureLayer.setVisible(false)
                            addFeatureLayer(databaseFeatureLayer)
                        }
                    }

                    ShpType.SHP_TYPR -> if (shp.isContainTpk) {
                        //shp文件是2000格式可以使用
                        val shapefileFeatureTable = ShapefileFeatureTable(shp.path)
                        shapefileFeatureTable.loadAsync()
                        val shpFeatureLayer = FeatureLayer(shapefileFeatureTable)
                        val labelDefinition: LabelDefinition =
                            LabelDefinition.fromJson("{\"allowOverrun\":true,\"labelExpressionInfo\":{\"expression\":\"\$feature.BCDYH\"},\"symbol\":{\"angle\":0,\"backgroundColor\":[0,0,0,0],\"borderLineColor\":[255,255,255,255],\"borderLineSize\":0,\"color\":[0,0,255,255],\"font\":{\"decoration\":\"none\",\"size\":8.25,\"style\":\"normal\",\"weight\":\"normal\"},\"haloColor\":[255,255,255,255],\"haloSize\":1.5,\"horizontalAlignment\":\"center\",\"kerning\":false,\"type\":\"esriTS\",\"verticalAlignment\":\"middle\",\"xoffset\":0,\"yoffset\":0},\"useCodedValues\":true}")
                        shpFeatureLayer.setLabelsEnabled(true)
                        shpFeatureLayer.getLabelDefinitions().add(labelDefinition)
                        shp.name=shpFeatureLayer.getName()
                        // 设置Shapefile文件的渲染方式
                        val lineSymbol =
                            SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 1.0f)
                        //fillSymbol为填充样式
                        val fillSymbol = SimpleFillSymbol(
                            SimpleFillSymbol.Style.SOLID,
                            Color.TRANSPARENT,
                            lineSymbol
                        )
                        val renderer = SimpleRenderer(fillSymbol)
                        shpFeatureLayer.setRenderer(renderer)
                        mapView!!.map.operationalLayers.add(shpFeatureLayer)
                        //shp  默认隐藏所有
                        val operationalLayers: LayerList = mapView!!.map.operationalLayers
                        for (layer in operationalLayers) {
                            layer.isVisible = false
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //xd项目
    fun showBUCHANDANYUANPaletteGraphics() {
        bcdyGraphicsLayer?.setVisible(true)
    }

    fun goneBUCHANDANYUANPaletteGraphics() {
        bcdyGraphicsLayer?.setVisible(false)
    }

    //富新街
    fun showFUXINJIEPaletteGraphics() {
        fxjGraphicsLayer?.setVisible(true)
    }

    fun goneFUXINJIEPaletteGraphics() {
        fxjGraphicsLayer?.setVisible(false)
    }

    //延河水库
    fun showYHSKPaletteGraphics() {
        yhskGraphicsLayer?.setVisible(true)
    }

    //延河水库
    fun goneYHSKPaletteGraphics() {
        yhskGraphicsLayer?.setVisible(false)
    }

    //可设置查看全幅
    fun layerWholePicture() {
        try {
            mapView!!.setViewpointRotationAsync(0.0)
            mapView!!.setViewpointAsync(mapView!!.map.initialViewpoint)
        } catch (ex: Exception) {
            Log.e(ex.message.toString(), "全图错误")
        }
    }

    private var xmlDataHelper: XmlDataHelper? = null

    fun setXmlDataHelper(xmlDataHelper: XmlDataHelper?) {
        this.xmlDataHelper = xmlDataHelper
    }
}