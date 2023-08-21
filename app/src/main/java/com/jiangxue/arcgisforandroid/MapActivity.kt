package com.jiangxue.arcgisforandroid

import android.Manifest
import android.graphics.Color
import android.graphics.Point
import android.location.Location
import android.util.Log
import android.view.View
import com.esri.arcgisruntime.concurrent.ListenableFuture
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Created by JiangXue on 2022/5/12.
 * 影像单元绘制
 */
@RuntimePermissions
class MapActivity : BaseActivity(), LocationListener, ActionView.DismissListener,
    XmlDataHelper.BasicDataPrepareCallback, IsendEasyGeoResponse,
    IGetProjectTempUnitDetailResponse {
    private var sketchEditor: SketchEditor? = null

    //默认是面
    private val sketchCreationMode: SketchCreationMode = SketchCreationMode.POLYGON

    //    @BindView(R.id.map_view)
    //    MapView mapView;
    //    @BindView(R.id.zoom_location_view)
    //    ZoomAndLocationView zoomAndLocationView;
    //    @BindView(R.id.action_view)
    //    ActionView actionView;
    //    @BindView(R.id.coordinates)
    //    TextView coordinates;
    //    @BindView(R.id.ll_layer_control)
    //    LinearLayout llLayercontrol;//项目控制
    private var mapViewHelper: MapViewHelper? = null
    private var locationHelper: LocationHelper? = null
    private var baseMapChangeView: BaseMapChangeView? = null
    private var xmlDataHelper: XmlDataHelper? = null
    private var layerControlView: LayerControlView? = null
    private var windowManager: WindowManager? = null
    private var sendEasyGeoDataPresenter //上传绘图坐标点
            : SendEasyGeoDataPresenter? = null
    private var getProjectTemperUnitInfoPresenter //获取临时单元数据
            : GetProjectTemperUnitInfoPresenter? = null
    private var unitnum //临时单元号
            : String? = null
    private var status = false
    private var isjoin = false
    private var sunitBuilder: StringBuilder? = null
    private var map: ArcGISMap? = null
    private var binding: MapArcgisBinding? = null
    var geometry: Geometry? = null
    var allPolygon: MutableList<Polygon?>? = null
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MapArcgisBinding.inflate(getLayoutInflater())
        //setContentView(R.layout.map_arcgis);
        setContentView(binding.getRoot())
        ButterKnife.bind(this)
        map = ArcGISMap()
        //binding.mapView
        binding.mapView.setMap(map)
        sketchEditor = SketchEditor()
        binding.mapView.setSketchEditor(sketchEditor)
        binding.mapView.setAttributionTextVisible(false)
        initViewEvent()
        unitnum = getIntent().getStringExtra("temp_unit") //临时单元号
        status = getIntent().getBooleanExtra("status", false) //数据当前状态
        windowManager = getWindowManager()
        xmlDataHelper = XmlDataHelper()
        mapViewHelper = MapViewHelper(binding.mapView)
        locationHelper = LocationHelper(this)
        //已绘画完成的单元
        allPolygon = ArrayList<Polygon?>()
        //设置点击事件
        binding.mapView.setOnTouchListener(object :
            DefaultMapViewOnTouchListener(this, binding.mapView) {
            fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (!xmlDataHelper.isDataIsPrepared()) {
                    return false
                }
                val screenPoint = Point(e.getX().toInt(), e.getY().toInt())
                val point1: Point = mMapView.screenToLocation(screenPoint)
                val format = DecimalFormat("0.000000")
                val wgsPoint: Point =
                    GeometryEngine.project(point1, SpatialReference.create(4326)) as Point
                if (wgsPoint != null) {
                    binding.coordinates.setText(
                        "x : " + format.format(wgsPoint.getX()) + "   y : " + format.format(
                            wgsPoint.getY()
                        )
                    )
                }
                return super.onSingleTapConfirmed(e)
            }
        })
        mapViewHelper.setXmlDataHelper(xmlDataHelper)
        MapActivityPermissionsDispatcher.readPropertiesWithCheck(this)
        sendEasyGeoDataPresenter = SendEasyGeoDataPresenter()
        getProjectTemperUnitInfoPresenter = GetProjectTemperUnitInfoPresenter()
        getProjectTemperUnitInfoPresenter.getTemPerUnitDetail(this)
        if (status) {
            binding.startDraw.setVisibility(View.GONE)
            binding.fabSumbmit.setVisibility(View.GONE)
        } else {
            binding.startDraw.setVisibility(View.VISIBLE)
            binding.fabSumbmit.setVisibility(View.VISIBLE)
        }
    }

    private fun initData() {
        val baseMaps: List<BaseMap> = xmlDataHelper.getProperties().getBaseMap()
        if (!Check.isEmpty(baseMaps)) {
            if (baseMapChangeView == null) {
                baseMapChangeView = BaseMapChangeView(getContext())
                baseMapChangeView.setMapViewHelper(mapViewHelper)
                baseMapChangeView.setDismissListener(this)
                baseMapChangeView.showList(xmlDataHelper.getBaseMap(), object : Callback() {
                    fun select(baseMap: BaseMap?) {
                        mapViewHelper.setBaseMap(baseMap)
                        mapViewHelper.baseMapWholePicture()
                    }
                })
                baseMapChangeView.setSelect(0)
            }
            mapViewHelper.setShps(xmlDataHelper.getAllShps())
            //读取临时单元数据
            // viewModel.initLocalGeoJson(binding.mapView);
        }
    }

    private fun initViewEvent() {
        //开始绘制
        binding.startDraw.setOnClickListener(View.OnClickListener {
            sketchEditor.start(sketchCreationMode)
            showToast("开始绘制")
            binding.drawLayout.setVisibility(View.VISIBLE)
        })
        //取消当前绘制
        binding.stopDraw.setOnClickListener(View.OnClickListener {
            sketchEditor.stop()
            showToast("取消绘制")
            binding.drawLayout.setVisibility(View.INVISIBLE)
        })

        //绘图完成并提交
        binding.fabSumbmit.setOnClickListener(View.OnClickListener {
            if (sketchEditor.isSketchValid()) {
                geometry = sketchEditor.getGeometry()
                showToast(getResources().getString(R.string.drawfinish))
                sendToPc(geometry)
            } else {
                ToastUtils.showTopLong(getResources().getString(R.string.pleasecheck))
            }
        })


        //图层控制
        binding.cotrolllayer.setOnClickListener(View.OnClickListener {
            layerControl()
            showToast("图层控制")
        })
        //监听菜单按键
        binding.btnMenu.setOnMenuToggleListener(object : OnMenuToggleListener() {
            fun onMenuToggle(opened: Boolean) {
                if (!opened) {
                    binding.drawLayout.setVisibility(View.INVISIBLE)
                    //如果有未保存的绘图则直接删除
                    geometry = null
                    sketchEditor.stop()
                }
            }
        })

        //取消一个点
        binding.undo.setOnClickListener { view -> sketchEditor.undo() }
        //前进一个点
        binding.redo.setOnClickListener { view -> sketchEditor.redo() }
    }

    //判断是否与原地块中单元相交
    private fun checkShpUnit(mapView: MapView, gmt: Geometry): Boolean {
        isjoin = false
        val query = QueryParameters()
        query.setGeometry(gmt)
        for (layer in mapView.getMap().getOperationalLayers()) {
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

    fun sendToPc(geometry: Geometry?) {
        if (null == geometry) {
            ToastUtils.showLong(getResources().getString(R.string.drawinvalid))
            return
        }
        sunitBuilder = StringBuilder()
        val gmt: Geometry = GeometryEngine.project(geometry, SpatialReference.create(4531))
        val s: String = gmt.toJson() //4531坐标系
        val gson = Gson()
        val jsonRootBean: JsonRootBean = gson.fromJson(s, JsonRootBean::class.java)
        val lists: List<List<Double>> = jsonRootBean.getRings().get(0)
        for (i in lists.indices) {
            val x = lists[i][0]
            val y = lists[i][1]
            val nf: NumberFormat = DecimalFormat("###########.00000000")
            val xx = nf.format(x)
            val yy = nf.format(y)
            sunitBuilder = sunitBuilder!!.append("$xx,$yy").append(";")
        }
        sunitBuilder!!.substring(0, sunitBuilder!!.length - 1)
        sendEasyGeoDataPresenter.sendEasyGeoProject(this, sunitBuilder.toString())
    }

    //图层控制
    fun layerControl() {
        if (layerControlView == null) {
            layerControlView = LayerControlView(getContext())
            layerControlView.setDismissListener(this)
        }
        layerControlView.setData(mapViewHelper, xmlDataHelper.getLayerCategoryBeanList())
        binding.actionView.setDeafult()
        binding.actionView.showView(layerControlView)
    }

    @NeedsPermission([Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun location() {
        locationHelper.startLocation()
    }

    @NeedsPermission([Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE])
    fun readProperties() {
        xmlDataHelper.prepareBasicData(this)
    }

    protected fun onResume() {
        super.onResume()
        binding.mapView.resume()
    }

    fun onPause() {
        super.onPause()
        binding.mapView.pause()
    }

    fun onDestroy() {
        super.onDestroy()
        binding.mapView.dispose()
    }

    fun dismiss() {
        binding.actionView.dismiss()
    }

    fun prepareSuccess() {
        initData()
    }

    @OnClick([R.id.tv_back, R.id.ll_layer_control])
    fun OnClick(view: View) {
        when (view.id) {
            R.id.ll_layer_control -> layerControl()
            R.id.tv_back -> finish()
        }
    }

    fun sendEasyGeoSuccess(msg: String) {
        if (!StringUtils.isEmpty(msg)) {
            if ("0" == msg) {
                //0 没有相交项
                val intent = Intent()
                intent.putExtra("flag", true)
                intent.putExtra("unitBuilder", sunitBuilder.toString())
                setResult(RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    getResources().getString(R.string.cantjoin),
                    Toast.LENGTH_SHORT
                ).show()
                sketchEditor.stop()
            }
        }
    }

    fun sendEasyGeoFail(msg: String?) {
        //校验出错
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun getProjectTempUnitSuccess(attributes: List<Attributes?>?) {
        val graphicsOverlay = GraphicsOverlay()
        val polygonconer = PointCollection(SpatialReference.create(4531))
        binding.mapView.getGraphicsOverlays().add(graphicsOverlay)
        if (attributes == null || attributes.isEmpty()) {
            return
        }
        //图形绘制
        for (attr in attributes) {
            polygonconer.clear()
            var polygon: Polygon? = null
            if (!attr.getGEODATA().isEmpty()) {
                val geodata: String = attr.getGEODATA()
                val sourceArray =
                    geodata.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                for (i in sourceArray.indices) {
                    if (!TextUtils.isEmpty(sourceArray[i])) {
                        val geoArray =
                            sourceArray[i].split(",".toRegex()).dropLastWhile { it.isEmpty() }
                                .toTypedArray()
                        polygonconer.add(
                            Point(
                                java.lang.Double.valueOf(geoArray[0]), java.lang.Double.valueOf(
                                    geoArray[1]
                                )
                            )
                        )
                    }
                }
                polygon = Polygon(polygonconer)
                allPolygon!!.add(polygon) //所有polygon集合
                val centerPoint: Point = polygon.getExtent().getCenter()
                val pGraphicsOverlay: GraphicsOverlay = getGraphicsOverlay(binding.mapView)
                addText(pGraphicsOverlay, centerPoint, attr.getCODEID())
            }
            val lineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.GREEN, 1.0f)
            val simpleFillSymbol = SimpleFillSymbol(
                SimpleFillSymbol.Style.SOLID,
                Color.parseColor("#33e97676"),
                lineSymbol
            )
            val graphic = Graphic(polygon, simpleFillSymbol)
            graphicsOverlay.getGraphics().add(graphic)
        }
    }

    //图层获取
    private fun getGraphicsOverlay(mapView: MapView): GraphicsOverlay {
        val num: Int = mapView.getGraphicsOverlays().size()
        return if (num < 1) {
            val graphicsOverlay = GraphicsOverlay()
            //add the overlay to the map view
            mapView.getGraphicsOverlays().add(graphicsOverlay)
            graphicsOverlay
        } else {
            mapView.getGraphicsOverlays().get(0)
        }
    }

    //create text symbols
    private fun addText(graphicOverlay: GraphicsOverlay, point: Point, Text: String) {
        val bassRockSymbol = TextSymbol(
            10, Text, Color.rgb(0, 0, 230),
            TextSymbol.HorizontalAlignment.CENTER,
            TextSymbol.VerticalAlignment.MIDDLE
        )
        //define a graphic from the geometry and symbol
        val bassRockGraphic = Graphic(point, bassRockSymbol)
        //add the text to the graphics overlay
        graphicOverlay.getGraphics().add(bassRockGraphic)
    }

    fun getProjectTempUnitFail(message: String?) {}
    fun prepareError(errorMsg: String?) {}
    fun onLocationChanged(location: Location?) {
        //mapViewHelper.setLocation(location.getLatitude(), location.getLongitude(), getLocationDrawable(true));
    }

    fun dismissAndClear() {}
    fun dismissCallOut(isClose: Boolean) {}

    companion object {
        private const val DOUBLE_CLICK_TIME = 0.0
    }
}