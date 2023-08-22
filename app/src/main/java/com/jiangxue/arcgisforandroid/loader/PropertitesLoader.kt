package com.jiangxue.arcgisforandroid.loader

import com.jiangxu.PropertiesXmlAdapter
import com.jiangxue.arcgisforandroid.ArcgisAndroidApplication
import com.jiangxue.arcgisforandroid.adapter.XmlBaseAdapter
import com.jiangxue.arcgisforandroid.data.xml.properties.Properties


/**
 * 特性加载
 */
class PropertitesLoader private constructor() {
    init {
        if (null != instance) throw RuntimeException("this is singleton.")
    }

    fun loadPropertites(
        filePath: String?,
        propertiesLoaderCallback: XmlLoader.LoaderCallback<Any>,
    ) {
        XmlLoader.getInstance()?.load<Any>(
            filePath.toString(),
            PropertiesXmlAdapter(ArcgisAndroidApplication.getPath()),
            propertiesLoaderCallback
        )

    }

    companion object {
        /**
         * 单例对象实例
         */
        private var instance: PropertitesLoader? = null
        fun getInstance(): PropertitesLoader? {
            if (instance == null) {
                synchronized(XmlLoader::class.java) {
                    if (instance == null) {
                        instance = PropertitesLoader()
                    }
                }
            }
            return instance
        }
    }
}