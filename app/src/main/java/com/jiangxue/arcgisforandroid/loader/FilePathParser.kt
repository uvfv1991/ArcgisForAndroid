package com.jiangxue.arcgisforandroid.loader

import com.jiangxue.arcgisforandroid.ArcgisAndroidApplication


/**
 * 文件路径解析
 */
object FilePathParser {
    fun getPropertiesFilePath(filePath: String): String {
        return ArcgisAndroidApplication.getPath()+ filePath
    }
}