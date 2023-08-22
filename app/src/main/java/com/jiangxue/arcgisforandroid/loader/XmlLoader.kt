package com.jiangxue.arcgisforandroid.loader

import com.jiangxue.arcgisforandroid.adapter.XmlBaseAdapter
import com.jiangxue.arcgisforandroid.adapter.XmlParser
import com.jiangxue.arcgisforandroid.helper.AsyncObserver
import org.xmlpull.v1.XmlPullParserException
import java.io.FileNotFoundException
import java.io.IOException


/**
 * 配置文件加载器
 */
class XmlLoader private constructor() {
    init {
        if (null != instance) throw RuntimeException("this is singleton.")
    }

    interface LoaderCallback<Any> {
        fun loadSuccess(t: Any)
        fun loadError(errorMsg: String)
    }

    fun <T : Any> load(
        filePath: String,
        xmlBaseAdapter: XmlBaseAdapter<*>?,
        tLoaderCallback: LoaderCallback<T>?,
    ) {
        if (tLoaderCallback == null) {
            return
        }
        try {
            val tXmlParser = XmlParser<T>()
            tXmlParser.setAdapter(xmlBaseAdapter as XmlBaseAdapter<T>)
            tXmlParser.asyncParse(filePath, object : AsyncObserver<T>() {
                override fun onSuccess(t: T) {
                    tLoaderCallback.loadSuccess(t)
                }
            })
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
            tLoaderCallback.loadError(filePath + "配置文件不正确")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            tLoaderCallback.loadError(filePath + "文件不存在")
        }
    }

    @Throws(XmlPullParserException::class, IOException::class)
    fun <T : Any> load(filePath: String?, xmlBaseAdapter: XmlBaseAdapter<T>?): Any {
        val tXmlParser = XmlParser<T>()
        tXmlParser.setAdapter(xmlBaseAdapter!!)
        return tXmlParser.parse(filePath)
    }

    companion object {
        /**
         * 单例对象实例
         */
        private var instance: XmlLoader? = null
        fun getInstance(): XmlLoader? {
            if (instance == null) {
                synchronized(XmlLoader::class.java) {
                    if (instance == null) {
                        instance = XmlLoader()
                    }
                }
            }
            return instance
        }
    }
}
