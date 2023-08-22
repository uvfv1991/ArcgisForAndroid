package com.jiangxue.arcgisforandroid.adapter

import com.blankj.utilcode.util.StringUtils
import com.jiangxue.arcgisforandroid.adapter.XmlBaseAdapter
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.Shp
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.ShpProperties
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException

/**
 * shp配置文件适配器
 */
public class ShpPropertiesXmlAdpter(citedPath: String?) : XmlBaseAdapter<ShpProperties?>(citedPath.toString()) {
    @Throws(XmlPullParserException::class, IOException::class)
    override fun getXmlData(parser: XmlPullParser?): ShpProperties? {
        var shpProperties: ShpProperties? = null
        var shp: Shp? = null
        var event: Int? = parser?.getEventType()
        while (event != XmlPullParser.END_DOCUMENT) {
            val tagName: String? = parser?.getName()
            when (event) {
                XmlPullParser.START_DOCUMENT -> {}
                XmlPullParser.START_TAG -> if (tagName.equals("properties", ignoreCase = true)) {
                    shpProperties = ShpProperties()
                    shpProperties.citedPath=citedPath
                } else if (tagName.equals("SHP", ignoreCase = true)) {
                    shp = Shp()
                    shp.citedPath=citedPath
                } else if (tagName.equals("name", ignoreCase = true)) {
                    shp?.name =parser?.nextText()
                } else if (tagName.equals("shp_path", ignoreCase = true)) {
                    shp?.setShpPath(parser?.nextText())
                } else if (tagName.equals("tpk_path", ignoreCase = true)) {
                    val nextText: String? = parser?.nextText()
                    shp?.isContainTpk =!StringUtils.isEmpty(nextText)
                    shp?.setTpkPath(nextText)
                } else if (tagName.equals("shp_config_path", ignoreCase = true)) {
                    shp?.setShpConfigPath(parser?.nextText())
                } else if (tagName.equals("shp_color", ignoreCase = true)) {
                    shp?.setLayerColor(parser?.nextText())
                } else if (tagName.equals("shp_line_color", ignoreCase = true)) {
                    shp?.setLineColor(parser?.nextText())
                } else if (tagName.equals("shp_line_width", ignoreCase = true)) {
                    shp?.setLineWidth(parser?.nextText())
                } else if (tagName.equals("icon_path", ignoreCase = true)) {
                    shp?.setIconPath(parser?.nextText())
                }

                XmlPullParser.END_TAG -> if (tagName.equals("SHP", ignoreCase = true)) {
                    shpProperties?.addShps(shp!!)
                }
            }
            event = parser?.next()
        }
        return shpProperties!!
    }
}