package com.jiangxue.arcgisforandroid.data.xml.properties

import android.os.Parcel
import android.os.Parcelable
import com.jiangxue.arcgisforandroid.data.xml.XmlPath
import com.jiangxue.arcgisforandroid.util.FileUtil


class Category : XmlPath, Parcelable {
    /**
     * Gets name.
     *
     * @return the name
     */
    /**
     * Sets name.
     *
     * @param name the name
     */
    /**
     * 类别名称
     */
    var name: String? = null
    /**
     * Gets category properties path.
     *
     * @return the category properties path
     */
    /**
     * Sets category properties path.
     *
     * @param categoryPath the category properties path
     */
    /**
     * 类别所在的文件夹路劲
     */
    var categoryPath: String? = null

    /**
     * 类别icon所在的文件路劲
     */
    private var iconPath: String? = null
    fun getIconPath(): String {
        return citedPath + iconPath
    }

    fun setIconPath(iconPath: String?) {
        this.iconPath = iconPath
    }



    fun getParrentPath(): String? {
        return FileUtil.getParentPath(citedPath, categoryPath)
    }
    val path: String
        get() = citedPath + categoryPath

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeString(categoryPath)
        dest.writeString(iconPath)
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        name = `in`.readString()
        categoryPath = `in`.readString()
        iconPath = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

}