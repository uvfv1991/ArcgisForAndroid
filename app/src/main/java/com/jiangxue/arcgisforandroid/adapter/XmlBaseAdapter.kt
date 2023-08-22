package com.jiangxue.arcgisforandroid.adapter

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


abstract class XmlBaseAdapter<T>
/**
 * 构造方法
 *
 * @param citedPath 引用这个配置文件的配置文件夹路径
 */(protected var citedPath: String) {
    /**
     * 正在解析数据
     *
     * @param parser
     * @return
     */
    @Throws(XmlPullParserException::class, IOException::class)
    abstract fun getXmlData(parser: XmlPullParser?): T
}
