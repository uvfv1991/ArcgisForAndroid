package com.jiangxue.arcgisforandroid.data.xml

import android.os.Parcel
import android.os.Parcelable
import com.blankj.utilcode.util.StringUtils

open class XmlPath : Parcelable {
    var citedPath: String? = null

   /* *//**
     * 获取引用这个文件(或文件夹)的文件(或文件夹)路径
     *
     * @return
     *//*
    open fun getCitedPath(): String? {
        return if (StringUtils.isEmpty(citedPath)) "" else citedPath
    }

    *//**
     * 设置引用这个文件(或文件夹)的文件(或文件夹)路径
     *
     * @return
     *//*
    open fun setCitedPath(citedPath: String?) {
        this.citedPath = citedPath
    }*/

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


    companion object CREATOR : Parcelable.Creator<XmlPath> {
        override fun createFromParcel(parcel: Parcel): XmlPath {
            return XmlPath(parcel)
        }

        override fun newArray(size: Int): Array<XmlPath?> {
            return arrayOfNulls(size)
        }
    }
}