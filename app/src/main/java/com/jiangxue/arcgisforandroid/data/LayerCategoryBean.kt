package com.jiangxue.arcgisforandroid.data

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.jiangxue.arcgisforandroid.data.xml.shpproperties.Shp


/**
 * Created by LiuT on 2017/5/3.
 *
 * 图层控制bean
 */
class LayerCategoryBean : Parcelable {
    private var isChecked = false
    private var categoryPath: String? = null
    private var citedPath: String? = null
    private var iconPath: String? = null
    private var name: String? = null
    private var layers: List<Shp>? = null
    fun isChecked(): Boolean {
        return isChecked
    }

    fun setChecked(checked: Boolean) {
        isChecked = checked
    }

    fun getCategoryPath(): String? {
        return categoryPath
    }

    fun setCategoryPath(categoryPath: String?) {
        this.categoryPath = categoryPath
    }

    fun getCitedPath(): String? {
        return citedPath
    }

    fun setCitedPath(citedPath: String?) {
        this.citedPath = citedPath
    }

    fun getIconPath(): String? {
        return iconPath
    }

    fun setIconPath(iconPath: String?) {
        this.iconPath = iconPath
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getLayers(): List<Shp>? {
        return layers
    }

    fun setLayers(layers: List<Shp>?) {
        this.layers = layers
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (isChecked) 1.toByte() else 0.toByte())
        dest.writeString(categoryPath)
        dest.writeString(citedPath)
        dest.writeString(iconPath)
        dest.writeString(name)
        dest.writeTypedList(layers)
    }

    protected constructor(`in`: Parcel) {
        isChecked = `in`.readByte().toInt() != 0
        categoryPath = `in`.readString()
        citedPath = `in`.readString()
        iconPath = `in`.readString()
        name = `in`.readString()
        layers = `in`.createTypedArrayList<Shp>(Shp.CREATOR)
    }

    companion object {
        @JvmField
        val CREATOR: Creator<LayerCategoryBean?> = object : Creator<LayerCategoryBean?> {
            override fun createFromParcel(source: Parcel): LayerCategoryBean? {
                return LayerCategoryBean(source)
            }

            override fun newArray(size: Int): Array<LayerCategoryBean?> {
                return arrayOfNulls(size)
            }
        }
    }
}