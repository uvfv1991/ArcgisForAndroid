package com.jiangxu

import com.jiangxue.arcgisforandroid.adapter.XmlBaseAdapter
import com.jiangxue.arcgisforandroid.data.xml.properties.BaseMap
import com.jiangxue.arcgisforandroid.data.xml.properties.Category
import com.jiangxue.arcgisforandroid.data.xml.properties.Properties
import com.jiangxue.arcgisforandroid.data.xml.properties.ShpCategory
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


class PropertiesXmlAdapter
/**
 * 构造方法
 *
 * @param citedPath 引用这个配置文件的配置文件夹路径
 */
    (citedPath: String?) : XmlBaseAdapter<Properties?>(citedPath!!) {
    @Throws(XmlPullParserException::class, IOException::class)
    override fun getXmlData(parser: XmlPullParser?): Properties {
        var baseMap: BaseMap? = null
        var shpCategory: ShpCategory? = null
        var properties: Properties? = null
        var category: Category? = null

        var event: Int? = parser?.getEventType()
        while (event != XmlPullParser.END_DOCUMENT) {
            val tagName: String = parser?.getName().toString()
            when (event) {
                XmlPullParser.START_DOCUMENT -> {}
                XmlPullParser.START_TAG -> if (tagName.equals("properties", ignoreCase = true)) {
                    properties = Properties()
                    properties.citedPath=citedPath
                } else if (tagName.equals("title", ignoreCase = true)) {
                    properties?.title =parser?.nextText()
                } else if (tagName.equals("LOGO", ignoreCase = true)) {
                    properties?.logoPath =parser?.nextText()
                } else if (tagName.equals("MAP", ignoreCase = true)) {
                    baseMap = BaseMap()
                    baseMap.citedPath=citedPath
                } else if (tagName.equals("name", ignoreCase = true)) {
                    baseMap?.name =(parser?.nextText())
                } else if (tagName.equals("mapType", ignoreCase = true)) {
                    baseMap?.setMapType(parser?.nextText().toString())
                } else if (tagName.equals("path", ignoreCase = true)) {
                    baseMap?.setMapPath(parser?.nextText())
                } else if (tagName.equals("map_icon", ignoreCase = true)) {
                    baseMap?.setIconPath(parser?.nextText())
                } else if (tagName.equals("placeholder", ignoreCase = true)) {
                    baseMap?.placeholder =parser?.nextText()
                }  else if (tagName.equals("SHP_CATEGORY", ignoreCase = true)) {
                    shpCategory = ShpCategory()
                    shpCategory.citedPath=citedPath
                } else if (tagName.equals("CATEGORY", ignoreCase = true)) {
                    category = Category()
                    category.citedPath=citedPath
                } else if (tagName.equals("category_name", ignoreCase = true)) {
                    category?.name =(parser?.nextText())
                } else if (tagName.equals("category_path", ignoreCase = true)) {
                    category?.categoryPath =parser?.nextText()
                } else if (tagName.equals("category_icon_path", ignoreCase = true)) {
                    category?.setIconPath(parser?.nextText())
                }

                XmlPullParser.END_TAG ->
                    if (tagName.equals("map_whole_pic")) {
                        //baseMap?.setWholePicture(wholePicture)
                    } else if (tagName.equals("MAP")) {
                        properties?.addBaseMap(baseMap)
                    }else if (tagName.equals("SHP_CATEGORY", ignoreCase = true)) {
                    properties?.shpCategory =shpCategory
                } else if (tagName.equals("CATEGORY", ignoreCase = true)) {
                    shpCategory?.addCategorie(category)
                }
            }
            event = parser?.next()
        }
        return properties!!
    }
}