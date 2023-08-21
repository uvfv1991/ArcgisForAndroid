package com.jiangxue.arcgisforandroid.loader

import haoyuan.com.qianguoqualitysafety.adapter.PropertiesXmlAdapter

/**
 * Created by Jinyu Zhang on 2017/5/5.
 * 特性加载
 */
class PropertitesLoader private constructor() {
    init {
        if (null != instance) throw RuntimeException("this is singleton.")
    }

    fun loadPropertites(
        filePath: String,
        propertiesLoaderCallback: XmlLoader.LoaderCallback<Properties>?
    ) {
        XmlLoader.Companion.getInstance()!!.load(
            filePath,
            PropertiesXmlAdapter(MyApplication.getWorkPath()),
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