package com.jiangxue.arcgisforandroid.data.xml.shpconfig

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Admin on 2017/4/28.
 */
class Fieds : Parcelable {
    var fied: List<Fied>? = null
    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeTypedList(fied)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        fied = `in`.createTypedArrayList<Fied>(Fied.Companion.CREATOR)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Fieds?> = object : Parcelable.Creator<Fieds?> {
            override fun createFromParcel(source: Parcel): Fieds? {
                return Fieds(source)
            }

            override fun newArray(size: Int): Array<Fieds?> {
                return arrayOfNulls(size)
            }
        }
    }
}