package com.jiangxue.arcgisforandroid.data.xml.properties

import haoyuan.com.qianguoqualitysafety.pojo.xml.XmlPath

/**
 * Created by Jinyu Zhang on 2017/5/4.
 * Shp文件类别
 */
class ShpCategory : XmlPath, Parcelable {
    private var categories: MutableList<Category> = ArrayList()
    fun getCategories(): List<Category> {
        return categories
    }

    fun addCategorie(category: Category) {
        categories.add(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList<Category>(categories)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        categories = `in`.createTypedArrayList<Category>(Category.Companion.CREATOR)
    }

    companion object {
        val CREATOR: Parcelable.Creator<ShpCategory> = object : Parcelable.Creator<ShpCategory?> {
            override fun createFromParcel(source: Parcel): ShpCategory {
                return ShpCategory(source)
            }

            override fun newArray(size: Int): Array<ShpCategory> {
                return arrayOfNulls(size)
            }
        }
    }
}