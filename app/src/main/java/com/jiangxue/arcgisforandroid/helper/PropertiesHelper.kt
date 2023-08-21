package com.jiangxue.arcgisforandroid.helper

import com.google.gson.Gson
import com.jiangxue.arcgisforandroid.ArcgisAndroidApplication
import com.jiangxue.arcgisforandroid.ArcgisAndroidApplication.Companion.context
import com.jiangxue.arcgisforandroid.data.Area
import com.jiangxue.arcgisforandroid.data.xml.properties.Properties
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * Created by Jinyu Zhang on 2017/5/5.
 * 特性帮助类
 */
class PropertiesHelper {
    interface PropertiesGetCallback {
        fun result(properties: Properties?)
    }

    var properties: Properties? = null
        private set
    private var propertiesGetCallback: PropertiesGetCallback? = null
    fun getProperties(propertiesGetCallback: PropertiesGetCallback?) {
        this.propertiesGetCallback = propertiesGetCallback
        if (propertiesGetCallback == null) {
            return
        }
        if (properties == null) {
            loadProperties()
        } else {
            propertiesGetCallback.result(properties)
        }
    }

    private fun loadProperties() {
        PropertitesLoader.getInstance().loadPropertites(
            context.toString() + "/jx.properties",
            object : XmlLoader.LoaderCallback<Properties?> {
                override fun loadSuccess(properties: Properties) {
                    this@PropertiesHelper.properties = properties
                    propertiesGetCallback!!.result(properties)
                }

                override fun loadError(errorMsg: String?) {}
            })
    }

    val zHENData: Area
        get() = Gson().fromJson(getLocation("/镇.txt"), Area::class.java)
    val yLYDData: Area
        get() = Gson().fromJson(getLocation("/已利用地编号.txt"), Area::class.java)
    val wLYDData: Area
        get() = Gson().fromJson(getLocation("/未利用地编号.txt"), Area::class.java)
    val hXData: Area
        get() = Gson().fromJson(getLocation("/HX编号.txt"), Area::class.java)
    val cUNData: Area
        get() = Gson().fromJson(getLocation("/村.txt"), Area::class.java)
    val bCDYData: Area
        get() = Gson().fromJson(getLocation("/补偿单元.txt"), Area::class.java)

    fun getAreaList(path: String): List<Area> {
        val list: MutableList<Area> = ArrayList()
        val root = File(ArcgisAndroidApplication.getPath() + path)
        if (root.isDirectory) {
            val files = root.listFiles()
            for (file in files) {
//                Log.e("eee", "file === " + file.getName());
                list.add(Gson().fromJson(getString(file), Area::class.java))
            }
        }
        return list
    }

    fun getLocation(path: String): String {
        val file = File(ArcgisAndroidApplication.getPath() + path)
        return getString(file)
    }

    private fun getString(file: File): String {
        var reader: BufferedReader? = null
        val sbf = StringBuffer()
        try {
            reader = BufferedReader(FileReader(file))
            var tempStr: String?
            while (reader.readLine().also { tempStr = it } != null) {
                sbf.append(tempStr)
            }
            reader.close()
            return sbf.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
        return ""
    }

    fun getAttr(path: String): List<String> {
        val list: MutableList<String> = ArrayList()
        val file = File(ArcgisAndroidApplication.getPath() + path)
        var reader: BufferedReader? = null
        val sbf = StringBuffer()
        try {
            reader = BufferedReader(FileReader(file))
            var tempStr: String
            while (reader.readLine().also { tempStr = it } != null) {
                sbf.append(tempStr)
                list.add(tempStr)
            }
            reader.close()
            return list
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }
            }
        }
        return list
    }
}