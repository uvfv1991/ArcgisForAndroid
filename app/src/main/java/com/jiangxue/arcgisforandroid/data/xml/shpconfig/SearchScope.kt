package com.jiangxue.arcgisforandroid.data.xml.shpconfig

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Admin on 2017/5/10.
 */
class SearchScope : Parcelable {
    var minScope = 0.0
        private set
    var maxScope = 0.0
        private set

    fun setMinScope(minScope: String) {
        this.minScope = minScope.toDouble()
    }

    fun setMaxScope(maxScope: String) {
        this.maxScope = maxScope.toDouble()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(minScope)
        dest.writeDouble(maxScope)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        minScope = `in`.readDouble()
        maxScope = `in`.readDouble()
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<SearchScope?> = object : Parcelable.Creator<SearchScope?> {
            override fun createFromParcel(source: Parcel): SearchScope? {
                return SearchScope(source)
            }

            override fun newArray(size: Int): Array<SearchScope?> {
                return arrayOfNulls(size)
            }
        }
    }
}