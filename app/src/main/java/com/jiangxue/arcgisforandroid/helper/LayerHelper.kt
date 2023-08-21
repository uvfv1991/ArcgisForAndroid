package com.jiangxue.arcgisforandroid.helper

import com.jiangxue.arcgisforandroid.data.xml.properties.Properties
import haoyuan.com.qianguoqualitysafety.adapter.ShpPropertiesXmlAdpter
import java.io.IOException

/**
 * Created by Jinyu Zhang on 2017/5/5.
 * 图层帮助类
 */
class LayerHelper(var properties: Properties) {
    interface Callback {
        fun result(layerCategoryBeanList: List<LayerCategoryBean>?)
        fun error(errorMsg: String?)
    }

    fun parse(callback: Callback?) {
        if (callback == null) {
            return
        }
        try {
            val layerCategoryBeans: MutableList<LayerCategoryBean> = ArrayList<LayerCategoryBean>()
            for (category in properties.shpCategory!!.getCategories()) {
                val xmlLoader: XmlLoader = XmlLoader.getInstance()
                val shpProperties: ShpProperties = xmlLoader.load(
                    category.getPath(),
                    ShpPropertiesXmlAdpter(category.getParrentPath())
                )
                val layerCategoryBean = LayerCategoryBean()
                layerCategoryBean.setName(category.getName())
                layerCategoryBean.setIconPath(category.getIconPath())
                layerCategoryBean.setLayers(shpProperties.getShps())
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