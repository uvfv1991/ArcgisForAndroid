package com.jiangxue.arcgisforandroid.loader

import haoyuan.com.qianguoqualitysafety.app.MyApplication

/**
 * Created by Administrator on 2017/5/5.
 * 文件路径解析
 */
object FilePathParser {
    fun getPropertiesFilePath(filePath: String): String {
        return MyApplication.getWorkPath() + filePath
    }
}