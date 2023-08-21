package com.jiangxue.arcgisforandroid.adapter

import haoyuan.com.qianguoqualitysafety.pojo.xml.shpproperties.Shp
import java.io.IOException

/**
 * Created by Jinyu Zhang on 2017/5/5.
 * shp配置文件适配器
 */
class ShpPropertiesXmlAdpter
/**
 * 构造方法
 *
 * @param citedPath 引用这个配置文件的配置文件夹路径
 */
    (citedPath: String?) : XmlBaseAdapter<ShpProperties?>(citedPath) {
    @Throws(XmlPullParserException::class, IOException::class)
    fun getXmlData(parser: XmlPullParser): ShpProperties {
        var shpProperties: ShpProperties? = null
        var shp: Shp? = null
        var event: Int = parser.getEventType()
        while (event != XmlPullParser.END_DOCUMENT) {
            val tagName: String = parser.getName()
            when (event) {
                XmlPullParser.START_DOCUMENT -> {}
                XmlPullParser.START_TAG -> if (tagName.equals("properties", ignoreCase = true)) {
                    shpProperties = ShpProperties()
                    shpProperties.setCitedPath(citedPath)
                } else if (tagName.equals("SHP", ignoreCase = true)) {
                    shp = Shp()
                    shp.setCitedPath(citedPath)
                } else if (tagName.equals("name", ignoreCase = true)) {
                    shp.setName(parser.nextText())
                } else if (tagName.equals("shp_path", ignoreCase = true)) {
                    shp.setShpPath(parser.nextText())
                } else if (tagName.equals("tpk_path", ignoreCase = true)) {
                    val nextText: String = parser.nextText()
                    shp.setContainTpk(!Check.isEmpty(nextText))
                    shp.setTpkPath(nextText)
                } else if (tagName.equals("shp_config_path", ignoreCase = true)) {
                    shp.setShpConfigPath(parser.nextText())
                } else if (tagName.equals("shp_color", ignoreCase = true)) {
                    shp.setLayerColor(parser.nextText())
                } else if (tagName.equals("shp_line_color", ignoreCase = true)) {
                    shp.setLineColor(parser.nextText())
                } else if (tagName.equals("shp_line_width", ignoreCase = true)) {
                    shp.setLineWidth(parser.nextText())
                } else if (tagName.equals("icon_path", ignoreCase = true)) {
                    shp.setIconPath(parser.nextText())
                }

                XmlPullParser.END_TAG -> if (tagName.equals("SHP", ignoreCase = true)) {
                    shpProperties.addShps(shp)
                }
            }
            event = parser.next()
        }
        return shpProperties
    }
}