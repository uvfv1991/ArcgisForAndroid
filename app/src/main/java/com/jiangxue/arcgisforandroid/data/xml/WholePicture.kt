package com.jiangxue.arcgisforandroid.data.xml

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Jinyu Zhang on 2017/5/10.
 */
class WholePicture : Parcelable {
    private var xMin = 0.0
    private var yMin = 0.0
    private var xMax = 0.0
    private var yMax = 0.0
    private var isValid = false
    fun getxMin(): Double {
        return xMin
    }

    fun setxMin(xMin: String) {
        this.xMin = xMin.toDouble()
    }

    fun getyMin(): Double {
        return yMin
    }

    fun setyMin(yMin: String) {
        this.yMin = yMin.toDouble()
    }

    fun getxMax(): Double {
        return xMax
    }

    fun setxMax(xMax: String) {
        this.xMax = xMax.toDouble()
    }

    fun getyMax(): Double {
        return yMax
    }

    fun setyMax(yMax: String) {
        this.yMax = yMax.toDouble()
    }

    fun isValid(): Boolean {
        return (xMin != 0.0) and (xMax != 0.0) and (yMin != 0.0) and (yMax != 0.0)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeDouble(xMin)
        dest.writeDouble(yMin)
        dest.writeDouble(xMax)
        dest.writeDouble(yMax)
        dest.writeByte(if (isValid) 1.toByte() else 0.toByte())
    }

    constructor() {}
    protected constructor(`in`: Parcel) {
        xMin = `in`.readDouble()
        yMin = `in`.readDouble()
        xMax = `in`.readDouble()
        yMax = `in`.readDouble()
        isValid = `in`.readByte().toInt() != 0
    }

    companion object {
        val CREATOR: Parcelable.Creator<WholePicture> = object : Parcelable.Creator<WholePicture?> {
            override fun createFromParcel(source: Parcel): WholePicture? {
                return WholePicture(source)
            }

            override fun newArray(size: Int): Array<WholePicture?> {
                return arrayOfNulls(size)
            }
        }
    }
}