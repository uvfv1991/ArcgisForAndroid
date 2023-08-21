package com.jiangxue.arcgisforandroid.util

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import android.os.StatFs
import android.os.storage.StorageManager
import android.util.Log
import androidx.core.os.EnvironmentCompat
import java.io.File
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by LiuTing on 2019/3/5.
 *
 * 内存工具类
 */
object StorageUtils {
    private val TAG = StorageUtils::class.java.simpleName
    fun getInStorage(context: Context): StorageBean? {
        val storageData = getStorageData(context) ?: return null
        for (storageBean in storageData) {
            if (!storageBean.removable) {
                return storageBean
            }
        }
        return null
    }

    fun getExtStroage(context: Context): StorageBean? {
        val storageData = getStorageData(context) ?: return null
        for (storageBean in storageData) {
            if (storageBean.removable) {
                return storageBean
            }
        }
        return null
    }

    fun getStorageData(pContext: Context): ArrayList<StorageBean>? {
        val storageManager = pContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        try {
            //得到StorageManager中的getVolumeList()方法的对象
            val getVolumeList = storageManager.javaClass.getMethod("getVolumeList")
            //---------------------------------------------------------------------

            //得到StorageVolume类的对象
            val storageValumeClazz = Class.forName("android.os.storage.StorageVolume")
            //---------------------------------------------------------------------
            //获得StorageVolume中的一些方法
            val getPath = storageValumeClazz.getMethod("getPath")
            val isRemovable = storageValumeClazz.getMethod("isRemovable")
            var mGetState: Method? = null
            //getState 方法是在4.4_r1之后的版本加的，之前版本（含4.4_r1）没有
            // （http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.4_r1/android/os/Environment.java/）
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                try {
                    mGetState = storageValumeClazz.getMethod("getState")
                } catch (e: NoSuchMethodException) {
                    e.printStackTrace()
                }
            }
            //---------------------------------------------------------------------

            //调用getVolumeList方法，参数为：“谁”中调用这个方法
            val invokeVolumeList = getVolumeList.invoke(storageManager)
            //---------------------------------------------------------------------
            val length = java.lang.reflect.Array.getLength(invokeVolumeList)
            val list = ArrayList<StorageBean>()
            for (i in 0 until length) {
                val storageValume =
                    java.lang.reflect.Array.get(invokeVolumeList, i) //得到StorageVolume对象
                val path = getPath.invoke(storageValume) as String
                val removable = isRemovable.invoke(storageValume) as Boolean
                var state: String? = null
                if (mGetState != null) {
                    state = mGetState.invoke(storageValume) as String
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        state = Environment.getStorageState(File(path))
                    } else {
                        state = if (removable) {
                            EnvironmentCompat.getStorageState(File(path))
                        } else {
                            //不能移除的存储介质，一直是mounted
                            Environment.MEDIA_MOUNTED
                        }
                        val externalStorageDirectory = Environment.getExternalStorageDirectory()
                        Log.e(TAG, "externalStorageDirectory==$externalStorageDirectory")
                    }
                }
                var totalSize: Long = 0
                var availaleSize: Long = 0
                if (Environment.MEDIA_MOUNTED == state) {
                    totalSize = getTotalSize(path)
                    availaleSize = getAvailableSize(path)
                }
                val msg = ("path==" + path
                        + " ,removable==" + removable
                        + ",state==" + state
                        + ",total size==" + totalSize + "(" + fmtSpace(totalSize) + ")"
                        + ",availale size==" + availaleSize + "(" + fmtSpace(availaleSize) + ")")
                Log.e(TAG, msg)
                val storageBean = StorageBean()
                storageBean.availableSize = availaleSize
                storageBean.totalSize = totalSize
                storageBean.mounted = state
                storageBean.path = path
                storageBean.removable = removable
                list.add(storageBean)
            }
            return list
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getTotalSize(path: String?): Long {
        return try {
            val statFs = StatFs(path)
            var blockSize: Long = 0
            var blockCountLong: Long = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = statFs.blockSizeLong
                blockCountLong = statFs.blockCountLong
            } else {
                blockSize = statFs.blockSize.toLong()
                blockCountLong = statFs.blockCount.toLong()
            }
            blockSize * blockCountLong
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun getAvailableSize(path: String?): Long {
        return try {
            val statFs = StatFs(path)
            var blockSize: Long = 0
            var availableBlocks: Long = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = statFs.blockSizeLong
                availableBlocks = statFs.availableBlocksLong
            } else {
                blockSize = statFs.blockSize.toLong()
                availableBlocks = statFs.availableBlocks.toLong()
            }
            availableBlocks * blockSize
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    const val A_GB: Long = 1073741824
    const val A_MB: Long = 1048576
    const val A_KB = 1024
    fun fmtSpace(space: Long): String {
        if (space <= 0) {
            return "0"
        }
        val gbValue = space.toDouble() / A_GB
        return if (gbValue >= 1) {
            String.format("%.2fGB", gbValue)
        } else {
            val mbValue = space.toDouble() / A_MB
            Log.e("GB", "gbvalue=$mbValue")
            if (mbValue >= 1) {
                String.format("%.2fMB", mbValue)
            } else {
                val kbValue = (space / A_KB).toDouble()
                String.format("%.2fKB", kbValue)
            }
        }
    }

    class StorageBean : Parcelable {
        var path: String? = null
        var mounted: String? = null
        var removable = false
        var totalSize: Long = 0
        var availableSize: Long = 0
        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(path)
            dest.writeString(mounted)
            dest.writeByte(if (removable) 1.toByte() else 0.toByte())
            dest.writeLong(totalSize)
            dest.writeLong(availableSize)
        }

        constructor() {}
        protected constructor(`in`: Parcel) {
            path = `in`.readString()
            mounted = `in`.readString()
            removable = `in`.readByte().toInt() != 0
            totalSize = `in`.readLong()
            availableSize = `in`.readLong()
        }

        companion object {
            val CREATOR: Parcelable.Creator<StorageBean> =
                object : Parcelable.Creator<StorageBean?> {
                    override fun createFromParcel(source: Parcel): StorageBean? {
                        return StorageBean(source)
                    }

                    override fun newArray(size: Int): Array<StorageBean?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }
}