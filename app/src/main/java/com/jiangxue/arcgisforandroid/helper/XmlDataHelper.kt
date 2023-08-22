package com.jiangxue.arcgisforandroid.helper

import android.util.Log
import com.jiangxue.arcgisforandroid.data.LayerCategoryBean
import com.jiangxue.arcgisforandroid.data.xml.properties.BaseMap
import com.jiangxue.arcgisforandroid.data.xml.properties.Properties
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.Shp
import com.jiangxue.arcgisforandroid.helper.PropertiesHelper.PropertiesGetCallback


/**
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
    val baseMap: List<BaseMap>?
        get() = if (properties == null) {
            null
        } else properties!!.baseMap

    var isDataIsPrepared = false
        private set

    fun prepareBasicData(basicDataPrepareCallback: BasicDataPrepareCallback?) {

        propertiesHelper.getProperties(object : PropertiesGetCallback {
            override fun result(properties: Properties?) {
                this@XmlDataHelper.properties = properties
                layerHelper = LayerHelper(properties!!)
                layerHelper!!.parse(object : LayerHelper.Callback {
                    override fun result(layerCategoryBeanList: List<LayerCategoryBean>) {
                        for (layerCategoryBean in layerCategoryBeanList) {
                            for (shp in layerCategoryBean.getLayers()!!) {
                                shps.add(shp!!)
                            }
                        }

                        this@XmlDataHelper.layerCategoryBeanList = layerCategoryBeanList

                        basicDataPrepareCallback?.prepareSuccess()
                    }

                    override fun error(errorMsg: String) {

                        basicDataPrepareCallback?.prepareError(errorMsg)
                    }
                })
            }
        })
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




    init {
        propertiesHelper = PropertiesHelper()
    }

}