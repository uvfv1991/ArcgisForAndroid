package com.jiangxue.arcgisforandroid.data.xml.properties

import android.os.Parcel
import android.os.Parcelable
import com.jiangxue.arcgisforandroid.data.xml.XmlPath

/**
 * Created by Admin on 2017/5/4.
 * Shp文件类别
 */
class ShpCategory : XmlPath, Parcelable {
    private var categories: MutableList<Category> = ArrayList()
    fun getCategories(): List<Category> {
        return categories
    }

    fun addCategorie(category: Category?) {
        categories.add(category!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList<Category>(categories)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        categories = `in`.createTypedArrayList<Category>(Category.CREATOR)!!
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShpCategory?> = object : Parcelable.Creator<ShpCategory?> {
            override fun createFromParcel(source: Parcel): ShpCategory {
                return ShpCategory(source)
            }

            override fun newArray(size: Int): Array<ShpCategory?> {
                return arrayOfNulls(size)
            }
        }
    }
}