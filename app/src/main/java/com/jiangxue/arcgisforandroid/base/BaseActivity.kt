package com.jiangxue.arcgisforandroid.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.jiangxue.arcgisforandroid.event.Message
import com.jiangxue.arcgisforandroid.util.ActivityCollector
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    protected var mBinding: DB? = null

    private var dialog: MaterialDialog? = null

    /**
     * 判断当前Activity是否在前台。
     */
    protected var isActive: Boolean = false

    /**
     * 当前Activity的实例。
     */
    protected var activity: Activity? = null

    /** 当前Activity的弱引用，防止内存泄露  */
    private var activityWR: WeakReference<Activity>? = null

    /**
     * 日志输出标志
     */
    protected val TAG: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        activityWR = WeakReference(activity!!)
        ActivityCollector.pushTask(activityWR)

        EventBus.getDefault().register(this)
        initViewDataBinding()
        // 注册 UI事件
        registorDefUIChange()
        initView(savedInstanceState)
        initData()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        isActive = true
    }

    override fun onPause() {
        super.onPause()

        isActive = false
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop: a")
    }

    override fun onDestroy() {
        super.onDestroy()

        activity = null
        ActivityCollector.rmoveTask(activityWR)
        EventBus.getDefault().unregister(this)
    }
    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()

    /**
     * DataBinding
     */
    private fun initViewDataBinding() {
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            mBinding = DataBindingUtil.setContentView(this, layoutId())
            mBinding?.lifecycleOwner = this
        } else {
            setContentView(layoutId())
        }
    }

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
    }

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    private fun showLoading() {
        if (dialog == null) {
            dialog = MaterialDialog(this)
                .cancelable(false)
                .cornerRadius(8f)
                // .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
                .lifecycleOwner(this)
            // .maxWidth(R.dimen.dialog_width)
        }
        dialog?.show()
    }

    /**
     * 关闭等待框
     */
    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }
}
