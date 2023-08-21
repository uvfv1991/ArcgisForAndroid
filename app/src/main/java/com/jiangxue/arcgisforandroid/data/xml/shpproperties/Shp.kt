package com.jiangxue.arcgisforandroid.data.xml.shpproperties

import android.graphics.Color
import android.util.Log
import haoyuan.com.qianguoqualitysafety.pojo.xml.WholePicture
import java.io.File

/**
 * Created by Jinyu Zhang on 2017/5/5.
 */
class Shp : XmlPath, Parcelable {
    /**
     * 界面是否checked状态缓存
     */
    var isChecked = false
    var tpkId: Long = 0
    var tpkName: String? = null
    var id //图层id
            : Long = 0
    var isContainTpk = false
    private var tpkPath: String? = null
    var name: String? = null
    private var shpPath: String? = null
    private var shpConfigPath: String? = null
    private var iconPath: String? = null
    var layerColorString: String? = null
        private set
    private var lineColor: String? = null
    private var lineWidth: String? = null
    private var wholePicture: WholePicture? = null
    fun getTpkPath(): String {
        return getCitedPath() + tpkPath
    }

    fun setTpkPath(tpkPath: String?) {
        this.tpkPath = tpkPath
    }

    fun getShpPath(): String {
        return getCitedPath() + shpPath
    }

    fun setShpPath(shpPath: String?) {
        this.shpPath = shpPath
    }

    val shpType: ShpType
        get() {
            val file = File(getShpPath())
            if (file.exists()) {
                val fileName = file.name
                val filePrefix = fileName.substring(fileName.lastIndexOf(".") + 1)
                if (!Check.isEmpty(filePrefix)) {
                    if (filePrefix.contains("geodatabase")) {
                        return ShpType.GEO_DATABASE
                    } else if (filePrefix.contains("shp")) {
                        return ShpType.SHP_TYPR
                    }
                }
            } else {
            }
            return ShpType.SHP_TYPR
        }

    fun getShpConfigPath(): String {
        return getCitedPath() + shpConfigPath
    }

    fun setShpConfigPath(shpConfigPath: String?) {
        this.shpConfigPath = shpConfigPath
    }

    fun getIconPath(): String {
        return getCitedPath() + iconPath
    }

    fun setIconPath(iconPath: String?) {
        this.iconPath = iconPath
    }

    val path: String
        get() = getShpPath()

    fun getLayerColor(): Int {
        val resultColor: Int
        resultColor = try {
            Color.parseColor(layerColorString)
        } catch (e: Exception) {
            Log.e("eee", "getLayerColor is exception !!!!! layerColor ==== " + layerColorString)
            RandomColor().randomColor()
        }
        setLayerColor(ColorUtils.getColorToRGBWithAlpha(resultColor))
        return resultColor
    }

    fun setLayerColor(layerColor: String?) {
        layerColorString = layerColor
    }

    fun getLineColor(): Int {
        val resultColor: Int
        resultColor = try {
            Color.parseColor(lineColor)
        } catch (e: Exception) {
            RandomColor().randomColor()
        }
        return resultColor
    }

    fun setLineColor(lineColor: String?) {
        this.lineColor = lineColor
    }

    fun getLineWidth(): Float {
        val resultLineWidth: Float
        resultLineWidth = if (lineWidth == null || lineWidth!!.isEmpty()) {
            2.0f
        } else {
            try {
                lineWidth!!.toFloat()
            } catch (e: Exception) {
                2.0f
            }
        }
        return resultLineWidth
    }

    fun setLineWidth(lineWidth: String?) {
        this.lineWidth = lineWidth
    }

    fun getWholePicture(): WholePicture? {
        return wholePicture
    }

    fun setWholePicture(wholePicture: WholePicture?) {
        this.wholePicture = wholePicture
    }

    constructor() {}

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        super.writeToParcel(dest, flags)
        dest.writeByte(if (isChecked) 1.toByte() else 0.toByte())
        dest.writeLong(tpkId)
        dest.writeLong(id)
        dest.writeByte(if (isContainTpk) 1.toByte() else 0.toByte())
        dest.writeString(name)
        dest.writeString(shpPath)
        dest.writeString(shpConfigPath)
        dest.writeString(iconPath)
        dest.writeString(layerColorString)
        dest.writeString(lineColor)
        dest.writeString(lineWidth)
        dest.writeParcelable(wholePicture, flags)
    }

    protected constructor(`in`: Parcel) : super(`in`) {
        isChecked = `in`.readByte().toInt() != 0
        tpkId = `in`.readLong()
        id = `in`.readLong()
        isContainTpk = `in`.readByte().toInt() != 0
        name = `in`.readString()
        shpPath = `in`.readString()
        shpConfigPath = `in`.readString()
        iconPath = `in`.readString()
        layerColorString = `in`.readString()
        lineColor = `in`.readString()
        lineWidth = `in`.readString()
        wholePicture = `in`.readParcelable(WholePicture::class.java.getClassLoader())
    }

    override fun toString(): String {
        return "Shp{" +
                "isChecked=" + isChecked +
                ", tpkId=" + tpkId +
                ", id=" + id +
                ", containTpk=" + isContainTpk +
                ", tpkPath='" + tpkPath + '\'' +
                ", name='" + name + '\'' +
                ", shpPath='" + shpPath + '\'' +
                ", shpConfigPath='" + shpConfigPath + '\'' +
                ", iconPath='" + iconPath + '\'' +
                ", layerColor='" + layerColorString + '\'' +
                ", lineColor='" + lineColor + '\'' +
                ", lineWidth='" + lineWidth + '\'' +
                ", wholePicture=" + wholePicture +
                '}'
    }

    companion object {
        val CREATOR: Parcelable.Creator<Shp> = object : Parcelable.Creator<Shp?> {
            override fun createFromParcel(source: Parcel): Shp {
                return Shp(source)
            }

            override fun newArray(size: Int): Array<Shp> {
                return arrayOfNulls(size)
            }
        }
    }
}