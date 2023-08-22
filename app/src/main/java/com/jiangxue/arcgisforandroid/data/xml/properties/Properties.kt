package com.jiangxue.arcgisforandroid.data.xml.properties

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import com.jiangxue.arcgisforandroid.data.xml.XmlPath


/**
 * Created by Admin on 2017/5/4.
 * 总配置文件
 */
class Properties : XmlPath, Parcelable {
    var title: String? = null
    var logoPath: String? = null
        get() = citedPath + field
    private var baseMaps: MutableList<BaseMap> = ArrayList()
    var shpCategory: ShpCategory? = null
    val baseMap: List<BaseMap>
        get() = baseMaps

    fun addBaseMap(baseMap: BaseMap?) {
        baseMaps.add(baseMap!!)
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeString(title)
        dest.writeTypedList<BaseMap>(baseMaps)
        dest.writeParcelable(shpCategory, flags)
    }
    constructor(parcel: Parcel) : super(parcel) {
        title = parcel.readString()
        baseMaps = parcel.createTypedArrayList<BaseMap>(BaseMap.CREATOR)!!
        shpCategory = parcel.readParcelable(ShpCategory::class.java.classLoader)
    }


    @JvmField
    val CREATOR: Creator<Properties> = object : Creator<Properties> {
        override fun createFromParcel(source: Parcel): Properties {
            return Properties(source)
        }

        override fun newArray(size: Int): Array<Properties?> {
            return arrayOfNulls(size)
        }
    }



}