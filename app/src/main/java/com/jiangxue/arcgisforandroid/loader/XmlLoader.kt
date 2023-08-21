package com.jiangxue.arcgisforandroid.loader

import haoyuan.com.qianguoqualitysafety.adapter.XmlBaseAdapter
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by Jinyu Zhang on 2017/5/5.
 * 配置文件加载器
 */
class XmlLoader private constructor() {
    init {
        if (null != instance) throw RuntimeException("this is singleton.")
    }

    interface LoaderCallback<T> {
        fun loadSuccess(t: T)
        fun loadError(errorMsg: String?)
    }

    fun <T> load(
        filePath: String,
        xmlBaseAdapter: XmlBaseAdapter?,
        tLoaderCallback: LoaderCallback<T>?
    ) {
        if (tLoaderCallback == null) {
            return
        }
        try {
            val tXmlParser: XmlParser<T> = XmlParser()
            tXmlParser.setAdapter(xmlBaseAdapter)
            tXmlParser.asyncParse(filePath, object : AsyncObserver<T>() {
                fun onSuccess(t: T) {
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
    fun <T> load(filePath: String?, xmlBaseAdapter: XmlBaseAdapter<T>?): T {
        val tXmlParser: XmlParser<T> = XmlParser()
        tXmlParser.setAdapter(xmlBaseAdapter)
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