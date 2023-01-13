package com.github.mminng.pagestate

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
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

    init {
        val rootView: ViewGroup
        val context: Context
        when (builder.target) {
            is Activity -> {
                context = builder.target
                rootView = builder.target.findViewById(android.R.id.content)
            }
            is Fragment -> {
                context = builder.target.requireContext()
                rootView = builder.target.view?.parent as ViewGroup
            }
            is View -> {
                context = builder.target.context
                if (builder.target.parent != null) {
                    rootView = builder.target.parent as ViewGroup
                } else {
                    throw IllegalArgumentException("The parent this view can't null.")
                }
            }
            else -> {
                throw IllegalArgumentException("The target type must be Fragment or Activity or View.")
            }
        }
        val childCount: Int = rootView.childCount
        val contentView: View
        var index = 0
        if (builder.target is View) {
            contentView = builder.target
            for (i in 0 until childCount) {
                if (rootView.getChildAt(i) == contentView) {
                    index = i
                    break
                }
            }
        } else {
            contentView = rootView.getChildAt(index)
        }
        rootView.removeView(contentView)
        _stateView = StateView(context)
        _stateView.setBackground(android.R.color.white)
        rootView.addView(_stateView, index, contentView.layoutParams)
        _stateView.setContentView(contentView)
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
        builder.pageCreateListener?.let { _stateView.setPageCreateListener(it) }
        builder.pageChangeListener?.let { _stateView.setPageChangeListener(it) }
    }

    fun showLoading() {
        _stateView.showLoading()
    }

    fun showContent() {
        _stateView.showContent()
    }

    fun showEmpty(message: String = "", @DrawableRes iconResId: Int = 0) {
        _stateView.showEmpty(message, iconResId)
    }

    fun showError(message: String = "", @DrawableRes iconResId: Int = 0) {
        _stateView.showError(message, iconResId)
    }

    fun setReloadListener(listener: () -> Unit) {
        _stateView.setReloadListener(listener)
    }

    fun setBackground(@DrawableRes resId: Int) {
        _stateView.setBackground(resId)
    }

    class Builder(val target: Any) {

        var loadingLayout: Int = StateView.DEFAULT_LOADING_LAYOUT
            private set
        var emptyLayout: Int = StateView.DEFAULT_EMPTY_LAYOUT
            private set
        var errorLayout: Int = StateView.DEFAULT_ERROR_LAYOUT
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

        fun setPageCreateListener(listener: PageCreateListener.() -> Unit) = apply {
            pageCreateListener = listener
        }

        fun setPageChangeListener(listener: PageChangeListener.() -> Unit) = apply {
            pageChangeListener = listener
        }

        fun setLoadingLayout(@LayoutRes layoutId: Int) = apply {
            loadingLayout = layoutId
        }

        fun setEmptyLayout(
            @LayoutRes layoutId: Int,
            @IdRes iconId: Int = 0,
            @IdRes textId: Int = 0,
            @IdRes clickId: Int = 0
        ) = apply {
            emptyLayout = layoutId
            emptyIconId = iconId
            emptyTextId = textId
            emptyClickId = clickId
        }

        fun setErrorLayout(
            @LayoutRes layoutId: Int,
            @IdRes iconId: Int = 0,
            @IdRes textId: Int = 0,
            @IdRes clickId: Int = 0
        ) = apply {
            errorLayout = layoutId
            errorIconId = iconId
            errorTextId = textId
            errorClickId = clickId
        }

        fun build() = PageStateManager(this)
    }
}