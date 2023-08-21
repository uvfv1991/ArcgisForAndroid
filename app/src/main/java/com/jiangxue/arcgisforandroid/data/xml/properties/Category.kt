package com.jiangxue.arcgisforandroid.data.xml.properties

import haoyuan.com.qianguoqualitysafety.pojo.xml.XmlPath

/**
 * Created by Jinyu Zhang on 2017/5/4.
 * 类别
 */
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
        return getCitedPath() + iconPath
    }

    fun setIconPath(iconPath: String?) {
        this.iconPath = iconPath
    }

    val path: String
        get() = getCitedPath() + categoryPath
    val parrentPath: String
        get() = FileUtil.getParentPath(getCitedPath(), categoryPath)

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

    companion object {
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category?> {
            override fun createFromParcel(source: Parcel): Category {
                return Category(source)
            }

            override fun newArray(size: Int): Array<Category> {
                return arrayOfNulls(size)
            }
        }
    }
}