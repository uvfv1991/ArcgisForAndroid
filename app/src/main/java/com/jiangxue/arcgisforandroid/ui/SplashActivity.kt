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

import android.Manifest
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.perfectplayer.event.MessageEvent
import com.jiangxue.arcgisforandroid.base.BaseActivity
import com.jiangxue.arcgisforandroid.base.NoViewModel
import com.jiangxue.arcgisforandroid.databinding.ActivitySplashBinding
import com.permissionx.guolindev.PermissionX
import kotlinx.coroutines.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.startActivity
/**
 *  author : jiangxue
 *  date : 2023/8/17 12:30
 *  description :
 */

class SplashActivity : BaseActivity<NoViewModel, ActivitySplashBinding>() {


    private val splashDuration = 3 * 1000L

    private val alphaAnimation by lazy {
        AlphaAnimation(0.5f, 1.0f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    private val scaleAnimation by lazy {
        ScaleAnimation(1f, 1.05f, 1f, 1.05f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }


    override fun layoutId(): Int {
       return R.layout.activity_splash
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

    override fun onStart() {
        super.onStart()
        initPermissionX()

    }
    @Subscribe
    fun onEvent(event: MessageEvent) {
        when (event.getType()) {
        }
    }
    private fun initPermissionX() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO,
            )
            .onExplainRequestReason { scope, deniedList ->
                val message = "ArcgisForAndroid需要您同意以下权限才能正常使用"
                scope.showRequestReasonDialog(deniedList, message, "确定", "取消")
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    startActivity<MapActivity>()
                    finish()

                } else {
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }
    companion object {

        /**
         * 是否首次进入APP应用
         */

    }
}
