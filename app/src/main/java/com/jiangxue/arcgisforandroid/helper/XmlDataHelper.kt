package com.jiangxue.arcgisforandroid.helper

import com.jiangxue.arcgisforandroid.data.Area
import com.jiangxue.arcgisforandroid.data.LayerCategoryBean
import com.jiangxue.arcgisforandroid.data.xml.properties.Properties
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.Shp

/**
 * Created by Jinyu Zhang on 2017/5/6.
 * xml数据解析类
 */
class XmlDataHelper {
    private val propertiesHelper: PropertiesHelper
    private var layerHelper: LayerHelper? = null

    interface BasicDataPrepareCallback {
        fun prepareSuccess()
        fun prepareError(errorMsg: String?)
    }

    var layerCategoryBeanList: List<LayerCategoryBean>? = null
        private set
    var properties: Properties? = null
        private set
    val baseMap: List<Any>?
        get() = if (properties == null) {
            null
        } else properties!!.baseMap
    var isDataIsPrepared = false
        private set

    fun prepareBasicData(basicDataPrepareCallback: BasicDataPrepareCallback?) {
        propertiesHelper.getProperties { properties ->
            this@XmlDataHelper.properties = properties
            layerHelper = LayerHelper(properties)
            layerHelper!!.parse(object : LayerHelper.Callback {
                override fun result(layerCategoryBeanList: List<LayerCategoryBean>) {
                    for (layerCategoryBean in layerCategoryBeanList) {
                        for (shp in layerCategoryBean.getLayers()!!) {
                            shps.add(shp)
                        }
                    }
                    //
                    this@XmlDataHelper.layerCategoryBeanList = layerCategoryBeanList
                    isDataIsPrepared = true
                    basicDataPrepareCallback?.prepareSuccess()
                }

                override fun error(errorMsg: String) {
                    isDataIsPrepared = false
                    basicDataPrepareCallback?.prepareError(errorMsg)
                }
            })
        }
    }

    var shps: MutableList<Shp> = ArrayList()
    val allShps: List<Shp>
        get() = shps

    fun getAllShps(layerCategoryBeanList: List<LayerCategoryBean>): List<Shp> {
        val shps: MutableList<Shp> = ArrayList()
        for (layerCategoryBean in layerCategoryBeanList) {
            for (shp in layerCategoryBean.getLayers()!!) {
                shps.add(shp)
            }
        }
        return shps
    }

    var zhenArea: Area? = null
        get() {
            if (field != null) {
                return field
            }
            field = propertiesHelper.zhenData
            return field
        }
        private set
    var cunArea: Area? = null
        get() {
            if (field != null) {
                return field
            }
            field = propertiesHelper.cunData
            return field
        }
        private set
    private var ylydArea: Area? = null
    private var wlydArea: Area? = null
    private var hxArea: Area? = null

    //获取XD补偿单元
    var bcdyArea: Area? = null
        get() {
            if (field != null) {
                return field
            }
            field = propertiesHelper.bcdyData
            return field
        }
        private set

    //获取富新街单元
    var fxjArea: Area? = null
        get() {
            if (field != null) {
                return field
            }
            field = propertiesHelper.bcdyData
            return field
        }
        private set

    init {
        propertiesHelper = PropertiesHelper()
    }

    val hXArea: Area?
        get() {
            if (hxArea != null) {
                return hxArea
            }
            hxArea = propertiesHelper.hxData
            return hxArea
        }
    val wLYDArea: Area?
        get() {
            if (wlydArea != null) {
                return wlydArea
            }
            wlydArea = propertiesHelper.wlydData
            return wlydArea
        }
    val yLYDArea: Area?
        get() {
            if (ylydArea != null) {
                return ylydArea
            }
            ylydArea = propertiesHelper.ylydData
            return ylydArea
        }

    fun getAreaList(path: String?): List<Area> {
        return propertiesHelper.getAreaList(path)
    }

    fun getAttr(path: String?): List<String> {
        return propertiesHelper.getAttr(path)
    }
}