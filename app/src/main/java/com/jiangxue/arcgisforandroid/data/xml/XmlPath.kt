package com.jiangxue.arcgisforandroid.data.xml

import haoyuan.com.qianguoqualitysafety.utils.Check

/**
 * Created by Jinyu Zhang on 2017/5/6.
 */
open class XmlPath : Parcelable {
    private var citedPath: String? = null

    /**
     * 获取引用这个文件(或文件夹)的文件(或文件夹)路径
     *
     * @return
     */
    fun getCitedPath(): String {
        return if (Check.isEmpty(citedPath)) "" else citedPath!!
    }

    /**
     * 设置引用这个文件(或文件夹)的文件(或文件夹)路径
     *
     * @return
     */
    fun setCitedPath(citedPath: String?) {
        this.citedPath = citedPath
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(citedPath)
    }

    protected constructor(`in`: Parcel) {
        citedPath = `in`.readString()
    }
}