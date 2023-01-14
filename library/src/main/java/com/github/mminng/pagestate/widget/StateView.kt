package com.github.mminng.pagestate.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Pair
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.github.mminng.pagestate.R
import com.github.mminng.pagestate.listener.PageChangeListener
import com.github.mminng.pagestate.listener.PageCreateListener
import com.github.mminng.pagestate.state.State

/**
 * Created by zh on 2022/12/20.
 */
internal class StateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    companion object {
        val DEFAULT_LOADING_LAYOUT: Int = R.layout.default_page_state_loading
        val DEFAULT_EMPTY_LAYOUT: Int = R.layout.default_page_state_empty
        val DEFAULT_ERROR_LAYOUT: Int = R.layout.default_page_state_error
    }

    private val loadingViewStub: ViewStub = ViewStub(context)
    private val emptyViewStub: ViewStub = ViewStub(context)
    private val errorViewStub: ViewStub = ViewStub(context)
    private val customViewStub: ViewStub = ViewStub(context)
    private var _contentView: View? = null
    private var _loadingView: View? = null
    private var _emptyView: View? = null
    private var _errorView: View? = null
    private var _customView: View? = null

    private var _emptyLayoutId: Int = 0
    private var _emptyIconId: Int = 0
    private var _emptyTextId: Int = 0
    private var _emptyClickId: Int = 0
    private var _errorLayoutId: Int = 0
    private var _errorIconId: Int = 0
    private var _errorTextId: Int = 0
    private var _errorClickId: Int = 0

    private var _pageCreateListener: PageCreateListener? = null
    private var _pageChangeListener: PageChangeListener? = null
    private var _reloadListener: (() -> Unit)? = null

    private var _hasCustom: Boolean = false
    private var _done: Boolean = false

    fun setContentView(contentView: View) {
        _contentView = contentView
    }

    fun setLoadingView(@LayoutRes layoutId: Int) {
        loadingViewStub.layoutResource = layoutId
        addView(loadingViewStub)
    }

    fun setEmptyView(
        @LayoutRes layoutId: Int,
        @IdRes iconId: Int = 0,
        @IdRes textId: Int = 0,
        @IdRes clickId: Int = 0
    ) {
        emptyViewStub.layoutResource = layoutId
        _emptyLayoutId = layoutId
        _emptyIconId = iconId
        _emptyTextId = textId
        _emptyClickId = clickId
        addView(emptyViewStub)
    }

    fun setErrorView(
        @LayoutRes layoutId: Int,
        @IdRes iconId: Int = 0,
        @IdRes textId: Int = 0,
        @IdRes clickId: Int = 0
    ) {
        errorViewStub.layoutResource = layoutId
        _errorLayoutId = layoutId
        _errorIconId = iconId
        _errorTextId = textId
        _errorClickId = clickId
        addView(errorViewStub)
    }

    fun setCustomLayout(@LayoutRes layoutId: Int) {
        if (layoutId > 0) {
            customViewStub.layoutResource = layoutId
            addView(customViewStub)
            _hasCustom = true
        }
    }

    fun showLoading() {
        if (_done || contains(_loadingView)) return
        if (removePage(_emptyView)) {
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView)
        }
        if (removePage(_errorView)) {
            _pageChangeListener?.onPageErrorChanged(false, _errorView)
        }
        if (!inflatePage(State.LOADING)) {
            addView(_loadingView)
        }
        _pageChangeListener?.onPageLoadingChanged(true, _loadingView)
    }

    fun showContent() {
        if (_done) return
        if (removePage(_loadingView)) {
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView)
        }
        if (removePage(_emptyView)) {
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView)
        }
        if (removePage(_errorView)) {
            _pageChangeListener?.onPageErrorChanged(false, _errorView)
        }
        addView(_contentView)
        _done = true
        background = null
        _pageChangeListener = null
        _pageCreateListener = null
        _reloadListener = null
    }

    fun showEmpty(message: String, @DrawableRes iconResId: Int) {
        if (_done || contains(_emptyView)) return
        if (removePage(_loadingView)) {
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView)
        }
        if (removePage(_errorView)) {
            _pageChangeListener?.onPageErrorChanged(false, _errorView)
        }
        if (!inflatePage(State.EMPTY)) {
            addView(_emptyView)
        }
        setPage(State.EMPTY, message, iconResId)
        _pageChangeListener?.onPageEmptyChanged(true, _emptyView)
    }

    fun showError(message: String, @DrawableRes iconResId: Int) {
        if (_done || contains(_errorView)) return
        if (removePage(_loadingView)) {
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView)
        }
        if (removePage(_emptyView)) {
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView)
        }
        if (!inflatePage(State.ERROR)) {
            addView(_errorView)
        }
        setPage(State.ERROR, message, iconResId)
        _pageChangeListener?.onPageErrorChanged(true, _errorView)
    }

    fun showCustom() {
        if (!_hasCustom || _done || contains(_customView)) return
        if (removePage(_loadingView)) {
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView)
        }
        if (removePage(_emptyView)) {
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView)
        }
        if (removePage(_errorView)) {
            _pageChangeListener?.onPageErrorChanged(false, _errorView)
        }
        if (!inflatePage(State.CUSTOM)) {
            addView(_customView)
        }
        _pageChangeListener?.onPageCustomChanged(true, _customView)
    }

    fun setPageCreateListener(listener: PageCreateListener.() -> Unit) {
        val pageCreatedListener = PageCreateListener()
        pageCreatedListener.listener()
        _pageCreateListener = pageCreatedListener
    }

    fun setPageChangeListener(listener: PageChangeListener.() -> Unit) {
        val pageChangeListener = PageChangeListener()
        pageChangeListener.listener()
        _pageChangeListener = pageChangeListener
    }

    fun setReloadListener(listener: () -> Unit) {
        _reloadListener = listener
    }

    fun setBackground(@DrawableRes resId: Int) {
        setBackgroundResource(resId)
    }

    private fun inflatePage(state: State): Boolean {
        when (state) {
            State.LOADING -> {
                return if (loadingViewStub.parent != null) {
                    _loadingView = loadingViewStub.inflate()
                    _pageCreateListener?.onPageLoadingCreated(_loadingView)
                    true
                } else {
                    false
                }
            }
            State.EMPTY -> {
                return if (emptyViewStub.parent != null) {
                    _emptyView = emptyViewStub.inflate()
                    if (_emptyLayoutId == DEFAULT_EMPTY_LAYOUT) {
                        invokeReload(_emptyView, R.id.state_empty_btn)
                    } else {
                        invokeReload(_emptyView, _emptyClickId)
                    }
                    _pageCreateListener?.onPageEmptyCreated(_emptyView)
                    true
                } else {
                    false
                }
            }
            State.ERROR -> {
                return if (errorViewStub.parent != null) {
                    _errorView = errorViewStub.inflate()
                    if (_errorLayoutId == DEFAULT_ERROR_LAYOUT) {
                        invokeReload(_errorView, R.id.state_error_btn)
                    } else {
                        invokeReload(_errorView, _errorClickId)
                    }
                    _pageCreateListener?.onPageErrorCreated(_errorView)
                    true
                } else {
                    false
                }
            }
            State.CUSTOM -> {
                return if (customViewStub.parent != null) {
                    _customView = customViewStub.inflate()
                    _pageCreateListener?.onPageCustomCreated(_customView)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun removePage(view: View?): Boolean {
        val pair: Pair<Boolean, Int> = containsAndPosition(view)
        return if (pair.first) {
            removeViewAt(pair.second)
            true
        } else {
            false
        }
    }

    private fun setPage(state: State, message: String, @DrawableRes iconResId: Int) {
        if (state == State.EMPTY) {
            if (_emptyLayoutId == DEFAULT_EMPTY_LAYOUT) {
                setPageIcon(_emptyView, R.id.state_empty_icon, iconResId)
                setPageMessage(_emptyView, R.id.state_empty_text, message)
            } else {
                setPageIcon(_emptyView, _emptyIconId, iconResId)
                setPageMessage(_emptyView, _emptyTextId, message)
            }
        } else if (state == State.ERROR) {
            if (_errorLayoutId == DEFAULT_ERROR_LAYOUT) {
                setPageIcon(_errorView, R.id.state_error_icon, iconResId)
                setPageMessage(_errorView, R.id.state_error_text, message)
            } else {
                setPageIcon(_errorView, _errorIconId, iconResId)
                setPageMessage(_errorView, _errorTextId, message)
            }
        }
    }

    private fun setPageIcon(view: View?, @IdRes viewId: Int, @DrawableRes iconId: Int) {
        if (view != null && viewId > 0 && iconId > 0) {
            val imageView: View = view.findViewById(viewId)
            if (imageView is ImageView) {
                imageView.setImageResource(iconId)
            }
        }
    }

    private fun setPageMessage(view: View?, @IdRes viewId: Int, message: String) {
        if (view != null && viewId > 0 && message.isNotBlank()) {
            val textView: View = view.findViewById(viewId)
            if (textView is TextView) {
                textView.text = message
            }
        }
    }

    private fun invokeReload(view: View?, @IdRes viewId: Int) {
        if (view != null && viewId > 0) {
            view.findViewById<View>(viewId).setOnClickListener {
                _reloadListener?.invoke()
            }
        }
    }

    private fun contains(view: View?): Boolean =
        if (view == null) false
        else indexOfChild(view) >= 0


    private fun containsAndPosition(view: View?): Pair<Boolean, Int> {
        if (view == null) return Pair.create(false, -1)
        val index: Int = indexOfChild(view)
        return Pair.create(index >= 0, index)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _pageChangeListener = null
        _pageCreateListener = null
        _reloadListener = null
    }
}