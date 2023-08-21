package com.jiangxue.arcgisforandroid.data.xml.properties

import haoyuan.com.qianguoqualitysafety.pojo.xml.XmlPath

/**
 * Created by Jinyu Zhang on 2017/5/4.
 * 总配置文件
 */
class Properties : XmlPath, Parcelable {
    var title: String? = null
    var logoPath: String? = null
        get() = getCitedPath() + field
    private var baseMaps: MutableList<BaseMap> = ArrayList()
    var shpCategory: ShpCategory? = null
    val baseMap: List<BaseMap>
        get() = baseMaps

    fun addBaseMap(baseMap: BaseMap) {
        baseMaps.add(baseMap)
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

    protected constructor(`in`: Parcel) : super(`in`) {
        title = `in`.readString()
        baseMaps = `in`.createTypedArrayList<BaseMap>(BaseMap.Companion.CREATOR)
        shpCategory = `in`.readParcelable<ShpCategory>(ShpCategory::class.java.classLoader)
    }

    companion object {
        val CREATOR: Parcelable.Creator<Properties> = object : Parcelable.Creator<Properties?> {
            override fun createFromParcel(source: Parcel): Properties {
                return Properties(source)
            }

            override fun newArray(size: Int): Array<Properties> {
                return arrayOfNulls(size)
            }
        }
    }
}