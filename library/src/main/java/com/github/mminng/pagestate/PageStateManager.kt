package com.github.mminng.pagestate

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.github.mminng.pagestate.listener.PageChangeListener
import com.github.mminng.pagestate.listener.PageCreateListener
import com.github.mminng.pagestate.widget.StateView

/**
 * Created by zh on 2023/1/6.
 */
class PageStateManager constructor(builder: Builder) {

    private var _stateView: StateView
    private var _rootView: ViewGroup
    private var _contentView: View
    var index: Int
    private var _isDone: Boolean = false

    init {
        _rootView = requireRootView(builder.target)
        _contentView = requireContentView(builder.target, _rootView)
        val context: Context = _rootView.context
        index = _rootView.indexOfChild(_contentView)
//        _contentView.visibility = View.INVISIBLE
        _rootView.removeView(_contentView)
        _stateView = StateView(context)
        _rootView.addView(_stateView, index, _contentView.layoutParams)
        _stateView.background = _contentView.background
        _stateView.setLoadingView(builder.loadingLayout)
        _stateView.setEmptyView(
            builder.emptyLayout,
            builder.emptyIconId,
            builder.emptyTextId,
            builder.emptyClickId
        )
        _stateView.setErrorView(
            builder.errorLayout,
            builder.errorIconId,
            builder.errorTextId,
            builder.errorClickId
        )
        _stateView.setCustomLayout(builder.customLayout)
        builder.pageCreateListener?.let { _stateView.setPageCreateListener(it) }
        builder.pageChangeListener?.let { _stateView.setPageChangeListener(it) }
    }

    fun showLoading() {
        if (_isDone) return
        _stateView.showLoading()
    }

    fun showContent() {
//        _contentView.visibility = View.VISIBLE
        _rootView.addView(_contentView, index)
        _rootView.removeView(_stateView)
        _isDone = true
    }

    fun showEmpty(message: String = "", @DrawableRes iconResId: Int = 0) {
        if (_isDone) return
        _stateView.showEmpty(message, iconResId)
    }

    fun showError(message: String = "", @DrawableRes iconResId: Int = 0) {
        if (_isDone) return
        _stateView.showError(message, iconResId)
    }

    fun showCustom() {
        if (_isDone) return
        _stateView.showCustom()
    }

    fun setReloadListener(listener: () -> Unit) {
        _stateView.setReloadListener(listener)
    }

    private fun requireRootView(target: Any): ViewGroup =
        when (target) {
            is Activity -> {
                target.findViewById(Window.ID_ANDROID_CONTENT)
            }
            is Fragment -> {
                target.requireView().parent as ViewGroup
            }
            is View -> {
                if (target.parent != null) {
                    target.parent as ViewGroup
                } else {
                    throw IllegalArgumentException("The parent this view can't null.")
                }
            }
            else -> throw IllegalArgumentException("The target type must be Fragment or Activity or View.")
        }

    private fun requireContentView(target: Any, rootView: ViewGroup): View =
        if (target is View) {
            target
        } else {
            rootView.getChildAt(0)
        }

    class Builder(val target: Any) {

        var loadingLayout: Int = 0
            private set
        var emptyLayout: Int = 0
            private set
        var errorLayout: Int = 0
            private set
        var customLayout: Int = 0
            private set
        var emptyIconId: Int = 0
            private set
        var emptyTextId: Int = 0
            private set
        var emptyClickId: Int = 0
            private set
        var errorIconId: Int = 0
            private set
        var errorTextId: Int = 0
            private set
        var errorClickId: Int = 0
            private set
        var pageCreateListener: (PageCreateListener.() -> Unit)? = null
            private set
        var pageChangeListener: (PageChangeListener.() -> Unit)? = null
            private set

        fun setLoadingLayout(@LayoutRes layoutId: Int) = apply {
            loadingLayout = layoutId
        }

        fun setEmptyLayout(
            @LayoutRes layoutId: Int,
            @IdRes iconId: Int = 0,/*support ImageView only*/
            @IdRes textId: Int = 0,/*support TextView only*/
            @IdRes clickId: Int = 0
        ) = apply {
            emptyLayout = layoutId
            emptyIconId = iconId
            emptyTextId = textId
            emptyClickId = clickId
        }

        fun setErrorLayout(
            @LayoutRes layoutId: Int,
            @IdRes iconId: Int = 0,/*support ImageView only*/
            @IdRes textId: Int = 0,/*support TextView only*/
            @IdRes clickId: Int = 0
        ) = apply {
            errorLayout = layoutId
            errorIconId = iconId
            errorTextId = textId
            errorClickId = clickId
        }

        fun setCustomLayout(@LayoutRes layoutId: Int) = apply {
            customLayout = layoutId
        }

        fun setPageCreateListener(listener: PageCreateListener.() -> Unit) = apply {
            pageCreateListener = listener
        }

        fun setPageChangeListener(listener: PageChangeListener.() -> Unit) = apply {
            pageChangeListener = listener
        }

        fun build() = PageStateManager(this)
    }
}