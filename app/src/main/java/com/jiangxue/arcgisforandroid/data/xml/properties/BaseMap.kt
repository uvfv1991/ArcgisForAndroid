package com.jiangxue.arcgisforandroid.data.xml.properties

import android.os.Parcel
import android.os.Parcelable
import com.jiangxue.arcgisforandroid.data.xml.WholePicture
import com.jiangxue.arcgisforandroid.data.xml.XmlPath


class BaseMap : XmlPath,Parcelable{
    /**
     * 用于listview中是否选中（标记状态）
     */
    var isChecked = false
    var name: String? = null

    /**
     * 0 未知   1 本地   2 在线    3 天地图
     *
     * @return
     */
    var mapType = 0
        private set
    private var mapPath: String? = null
    private var iconPath: String? = null
    var placeholder: String? = null
        get() = citedPath + field
    private var wholePicture: WholePicture? = null
    fun setMapType(mapType: String) {
        val type: Int
        type = try {
            mapType.toInt()
        } catch (e: Exception) {
            0
        }
        this.mapType = type
    }

    fun getMapPath(): String {
        return if (mapType == 1) citedPath + mapPath else mapPath!!
    }

    fun setMapPath(mapPath: String?) {
        this.mapPath = mapPath
    }

    fun getWholePicture(): WholePicture? {
        return wholePicture
    }

    fun setWholePicture(wholePicture: WholePicture?) {
        this.wholePicture = wholePicture
    }

    fun getIconPath(): String {
        return citedPath + iconPath
    }

    fun setIconPath(iconPath: String?) {
        this.iconPath = iconPath
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte(if (isChecked) 1.toByte() else 0.toByte())
        dest.writeString(name)
        dest.writeString(mapPath)
        dest.writeString(iconPath)
        dest.writeParcelable(wholePicture, flags)
    }

    protected constructor(`in`: Parcel) : super(`in`) {
        isChecked = `in`.readByte().toInt() != 0
        name = `in`.readString()
        mapPath = `in`.readString()
        iconPath = `in`.readString()
        wholePicture = `in`.readParcelable(WholePicture::class.java.getClassLoader())
    }

    companion object CREATOR : Parcelable.Creator<BaseMap> {
        override fun createFromParcel(parcel: Parcel): BaseMap {
            return BaseMap(parcel)
        }

        override fun newArray(size: Int): Array<BaseMap?> {
            return arrayOfNulls(size)
        }
    }

}