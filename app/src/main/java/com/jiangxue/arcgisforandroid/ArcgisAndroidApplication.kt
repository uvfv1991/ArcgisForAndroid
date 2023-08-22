/*
 * Copyright (c) 2020. vipyinzhiwei <vipyinzhiwei@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jiangxue.arcgisforandroid

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.StringUtils
import com.jiangxue.arcgisforandroid.util.StorageUtils
import com.jiangxue.arcgisforandroid.util.StorageUtils.getExtStroage
import com.jiangxue.arcgisforandroid.util.StorageUtils.getInStorage
import java.io.File
import java.security.AccessController.getContext

/**
 *  author : jiangxue
 *  date : 2023/8/0212:30
 *  description :
 */
class ArcgisAndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
    }


    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var workPath: String? = null
        fun getPath(): String? {
            val extStroage: StorageUtils.StorageBean? = StorageUtils.getExtStroage(context)
            val inStorage: StorageUtils.StorageBean? = StorageUtils.getInStorage(context)


            if (StringUtils.isEmpty(workPath)) {
                if (extStroage == null) {
                    workPath = inStorage?.path + "/JxData"
                } else {
                    workPath = extStroage.path + "/JxData"
                    if (!File(workPath).exists()) {
                        workPath = inStorage?.path + "/JxData"
                    }
                }
            }
            return workPath
        }
    }




}