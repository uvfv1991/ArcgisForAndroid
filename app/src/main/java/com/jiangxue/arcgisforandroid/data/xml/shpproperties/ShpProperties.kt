package com.jiangxue.arcgisforandroid.data.xml.shpproperties

import android.os.Parcel
import android.os.Parcelable
import com.jiangxue.arcgisforandroid.data.xml.XmlPath

/**
 * Created by Admin
 */
class ShpProperties : XmlPath, Parcelable {
    private var shps: MutableList<Shp> = ArrayList()
    fun getShps(): List<Shp> {
        return shps
    }

    fun addShps(shp: Shp) {
        shps.add(shp)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList<Shp>(shps)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        shps = `in`.createTypedArrayList<Shp>(Shp.Companion.CREATOR)!!
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShpProperties?> =
            object : Parcelable.Creator<ShpProperties?> {
                override fun createFromParcel(source: Parcel): ShpProperties {
                    return ShpProperties(source)
                }

                override fun newArray(size: Int): Array<ShpProperties?> {
                    return arrayOfNulls(size)
                }
            }
    }
}