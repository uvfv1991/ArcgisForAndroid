package com.jiangxue.arcgisforandroid.data.xml.shpconfig

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils

/**
 * Created by Admin on 2017/4/28.
 */
class Fied : Parcelable {
    var isSearchkey = false
        private set
    var fiedkey: String? = null
    var fiedname: String? = null
    private var fiedSearchType: FiedSearchType? = null
    var searchCondition: Any? = null
    fun setSearchkey(isSearchkey: String) {
        this.isSearchkey = !TextUtils.isEmpty(isSearchkey) && isSearchkey == "true"
    }

    fun getFiedSearchType(): FiedSearchType {
        return if (fiedSearchType == null) FiedSearchType.NONE else fiedSearchType!!
    }

    fun setFiedSearchType(fiedSearchType: String?) {
        this.fiedSearchType = FiedSearchType.getFiedSearchType(fiedSearchType!!)
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeByte(if (isSearchkey) 1.toByte() else 0.toByte())
        dest.writeString(fiedkey)
        dest.writeString(fiedname)
        dest.writeInt(if (fiedSearchType == null) -1 else fiedSearchType!!.ordinal)
    }

    protected constructor(`in`: Parcel) {
        isSearchkey = `in`.readByte().toInt() != 0
        fiedkey = `in`.readString()
        fiedname = `in`.readString()
        val tmpFiedSearchType = `in`.readInt()
        fiedSearchType =
            if (tmpFiedSearchType == -1) null else FiedSearchType.values().get(tmpFiedSearchType)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Fied?> = object : Parcelable.Creator<Fied?> {
            override fun createFromParcel(source: Parcel): Fied? {
                return Fied(source)
            }

            override fun newArray(size: Int): Array<Fied?> {
                return arrayOfNulls(size)
            }
        }
    }
}