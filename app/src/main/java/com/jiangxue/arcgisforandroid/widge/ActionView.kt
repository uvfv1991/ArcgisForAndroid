package com.jiangxue.arcgisforandroid.widge

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import com.blankj.utilcode.util.ScreenUtils
import com.jiangxue.arcgisforandroid.R


class ActionView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : RelativeLayout(context, attrs, defStyleAttr) {
    interface DismissListener {
        fun dismiss()
        fun dismissAndClear()
        fun dismissCallOut(isClose: Boolean)
    }

    private val layoutParams: RelativeLayout.LayoutParams
    var isShowing = false
        private set


    @Synchronized
    fun showView(view: View?) {
        showView(view, false)
    }

    var showView: View? = null
        private set
    private var inAnim: Animation? = null
    private var outAnim: Animation? = null
    init {
        layoutParams = RelativeLayout.LayoutParams(
            (ScreenUtils.getScreenWidth() / 3) as Int,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        layoutParams.setMargins(0, 10, 10, 10)
        setClickable(false);
       inAnim= AnimationUtils.loadAnimation(getContext(), R.anim.da_slide_in_right)
         outAnim=AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_out)
    }

    fun setDeafult() {
        layoutParams.width = (ScreenUtils.getScreenWidth() / 3)
        layoutParams.height = LayoutParams.MATCH_PARENT
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
        outAnim?.setAnimationListener(object : Animation.AnimationListener {
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