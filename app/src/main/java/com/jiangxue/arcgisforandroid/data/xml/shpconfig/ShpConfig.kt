package com.jiangxue.arcgisforandroid.data.xml.shpconfig

import android.os.Parcel
import android.os.Parcelable


class ShpConfig : Parcelable {
    var fieds: Fieds? = null
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeParcelable(fieds, flags)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        fieds = `in`.readParcelable(Fieds::class.java.classLoader)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ShpConfig?> = object : Parcelable.Creator<ShpConfig?> {
            override fun createFromParcel(source: Parcel): ShpConfig? {
                return ShpConfig(source)
            }

            override fun newArray(size: Int): Array<ShpConfig?> {
                return arrayOfNulls(size)
            }
        }
    }
}