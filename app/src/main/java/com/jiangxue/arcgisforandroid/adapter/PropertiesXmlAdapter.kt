package com.jiangxue.arcgisforandroid.adapter

import haoyuan.com.qianguoqualitysafety.pojo.xml.WholePicture
import java.io.IOException

/**
 * Created by Jinyu Zhang on 2017/5/4.
 * xml文件
 */
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
        var wholePicture: WholePicture? = null
        var event: Int = parser.getEventType()
        while (event != XmlPullParser.END_DOCUMENT) {
            val tagName: String = parser.getName()
            when (event) {
                XmlPullParser.START_DOCUMENT -> {}
                XmlPullParser.START_TAG -> if (tagName.equals("properties", ignoreCase = true)) {
                    properties = Properties()
                    properties.setCitedPath(citedPath)
                } else if (tagName.equals("title", ignoreCase = true)) {
                    properties.setTitle(parser.nextText())
                } else if (tagName.equals("LOGO", ignoreCase = true)) {
                    properties.setLogoPath(parser.nextText())
                } else if (tagName.equals("MAP", ignoreCase = true)) {
                    baseMap = BaseMap()
                    baseMap.setCitedPath(citedPath)
                } else if (tagName.equals("name", ignoreCase = true)) {
                    baseMap.setName(parser.nextText())
                } else if (tagName.equals("mapType", ignoreCase = true)) {
                    baseMap.setMapType(parser.nextText())
                } else if (tagName.equals("path", ignoreCase = true)) {
                    baseMap.setMapPath(parser.nextText())
                } else if (tagName.equals("map_icon", ignoreCase = true)) {
                    baseMap.setIconPath(parser.nextText())
                } else if (tagName.equals("placeholder", ignoreCase = true)) {
                    baseMap.setPlaceholder(parser.nextText())
                } else if (tagName.equals("map_whole_pic", ignoreCase = true)) {
                    wholePicture = WholePicture()
                } else if (tagName.equals("left", ignoreCase = true)) {
                    wholePicture.setxMin(parser.nextText())
                } else if (tagName.equals("bottom", ignoreCase = true)) {
                    wholePicture.setyMin(parser.nextText())
                } else if (tagName.equals("right", ignoreCase = true)) {
                    wholePicture.setxMax(parser.nextText())
                } else if (tagName.equals("top", ignoreCase = true)) {
                    wholePicture.setyMax(parser.nextText())
                } else if (tagName.equals("SHP_CATEGORY", ignoreCase = true)) {
                    shpCategory = ShpCategory()
                    shpCategory.setCitedPath(citedPath)
                } else if (tagName.equals("CATEGORY", ignoreCase = true)) {
                    category = Category()
                    category.setCitedPath(citedPath)
                } else if (tagName.equals("category_name", ignoreCase = true)) {
                    category.setName(parser.nextText())
                } else if (tagName.equals("category_path", ignoreCase = true)) {
                    category.setCategoryPath(parser.nextText())
                } else if (tagName.equals("category_icon_path", ignoreCase = true)) {
                    category.setIconPath(parser.nextText())
                }

                XmlPullParser.END_TAG -> if (tagName.equals("map_whole_pic", ignoreCase = true)) {
                    baseMap.setWholePicture(wholePicture)
                } else if (tagName.equals("MAP", ignoreCase = true)) {
                    properties.addBaseMap(baseMap)
                } else if (tagName.equals("SHP_CATEGORY", ignoreCase = true)) {
                    properties.setShpCategory(shpCategory)
                } else if (tagName.equals("CATEGORY", ignoreCase = true)) {
                    shpCategory.addCategorie(category)
                }
            }
            event = parser.next()
        }
        return properties
    }
}