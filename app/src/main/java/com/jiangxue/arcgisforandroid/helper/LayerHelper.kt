package com.jiangxue.arcgisforandroid.helper

import  com.jiangxue.arcgisforandroid.adapter.ShpPropertiesXmlAdpter
import com.jiangxue.arcgisforandroid.adapter.XmlBaseAdapter
import com.jiangxue.arcgisforandroid.data.LayerCategoryBean
import com.jiangxue.arcgisforandroid.data.xml.properties.Properties
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.ShpProperties
import com.jiangxue.arcgisforandroid.loader.XmlLoader
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

/**
 * 图层帮助类
 */
class LayerHelper(var properties: Properties) {
    public interface Callback {
        fun result(layerCategoryBeanList: List<LayerCategoryBean>)
        fun error(errorMsg: String)
    }

    fun parse(callback: Callback) {
        if (callback == null) {
            return
        }
        try {
            val layerCategoryBeans: MutableList<LayerCategoryBean> = ArrayList<LayerCategoryBean>()
            for (category in properties.shpCategory!!.getCategories()) {
                val xmlLoader: XmlLoader = XmlLoader.getInstance()!!
                val shpProperties: ShpProperties? = xmlLoader.load(
                    category.path,
                    ShpPropertiesXmlAdpter(category.getParrentPath())!! as XmlBaseAdapter<Any>?
                ) as ShpProperties
                val layerCategoryBean = LayerCategoryBean()
                layerCategoryBean.setName(category.name)
                layerCategoryBean.setIconPath(category.getIconPath())
                layerCategoryBean.setLayers(shpProperties?.getShps())
                layerCategoryBeans.add(layerCategoryBean)
            }
            callback.result(layerCategoryBeans)
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}