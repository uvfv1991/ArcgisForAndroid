package com.jiangxue.arcgisforandroid.widge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import haoyuan.com.qianguoqualitysafety.R

/**
 * Created by Jinyu Zhang on 2017/5/4.
 */
class ActionView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    interface DismissListener {
        fun dismiss()
        fun dismissAndClear()
        fun dismissCallOut(isClose: Boolean)
    }

    private val layoutParams: RelativeLayout.LayoutParams
    private val inAnim: Animation
    private val outAnim: Animation
    var isShowing = false
        private set

    fun setDeafult() {
        layoutParams.width = (WHUtils.getScreenWidth(getContext()) / 3f)
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT
    }

    fun setBig() {
        layoutParams.width = (WHUtils.getScreenWidth(getContext()) / 2.5f)
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT
    }

    fun setWidth(width: Int) {
        layoutParams.width = width
    }

    fun setHeight(height: Int) {
        layoutParams.height = height
    }

    @Synchronized
    fun showView(view: View?) {
        showView(view, false)
    }

    var showView: View? = null
        private set

    init {
        layoutParams = RelativeLayout.LayoutParams(
            (WHUtils.getScreenWidth(context) / 3f) as Int,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        layoutParams.setMargins(0, 10, 10, 10)
        setClickable(false)
        inAnim = AnimationUtils.loadAnimation(getContext(), R.anim.da_slide_in_right)
        outAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_out)
    }

    fun showView(view: View?, isDismissBefore: Boolean, isCallOut: Boolean) {
        if (isCallOut) {
            removeAllViews()
        }
        showView(view, false)
    }

    fun showView(view: View?, isDismissBefore: Boolean) {
        if (view == null) {
            return
        }
        if (getChildCount() == 0) {
            view.startAnimation(inAnim)
            addView(view)
            view.layoutParams = layoutParams
        } else {
            if (isDismissBefore) {
                removeAllViews()
                addView(view)
                view.visibility = View.VISIBLE
            } else {
                if (view == getChildAt(0)) {
                    for (x in 0 until getChildCount()) {
                        dismissView(view, null)
                    }
                } else {
                    dismissView(getChildAt(0), view)
                }
            }
        }
        showView = view
        isShowing = true
    }

    fun dismiss() {
        if (getChildCount() == 1) {
            dismissView(getChildAt(0), null)
        } else {
            removeAllViews()
        }
        showView = null
        isShowing = false
    }

    fun dismissView(view: View, newView: View?) {
        outAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                removeView(view)
                newView?.let { showView(it) }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(outAnim)
    }
}