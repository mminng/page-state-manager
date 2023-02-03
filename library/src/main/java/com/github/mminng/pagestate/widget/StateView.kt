package com.github.mminng.pagestate.widget

import android.content.Context
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.github.mminng.pagestate.listener.PageChangeListener
import com.github.mminng.pagestate.listener.PageCreateListener
import com.github.mminng.pagestate.state.State

/**
 * Created by zh on 2022/12/20.
 */
internal class StateView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), OnClickListener {

    private val loadingViewStub: ViewStub = ViewStub(context)
    private val emptyViewStub: ViewStub = ViewStub(context)
    private val errorViewStub: ViewStub = ViewStub(context)
    private val customViewStub: ViewStub = ViewStub(context)
    private var _loadingView: View? = null
    private var _emptyView: View? = null
    private var _errorView: View? = null
    private var _customView: View? = null

    private var _emptyIconId: Int = 0
    private var _emptyTextId: Int = 0
    private var _emptyClickId: Int = 0
    private var _errorIconId: Int = 0
    private var _errorTextId: Int = 0
    private var _errorClickId: Int = 0
    private var _customClickId: Int = 0

    private var _pageCreateListener: PageCreateListener? = null
    private var _pageChangeListener: PageChangeListener? = null
    private var _reloadListener: (() -> Unit)? = null

    private var _hasCustom: Boolean = false

    fun setLoadingView(@LayoutRes layoutId: Int) {
        loadingViewStub.layoutResource = layoutId
        addView(loadingViewStub)
    }

    fun setEmptyView(
        @LayoutRes layoutId: Int,
        @IdRes iconId: Int = 0,/*support ImageView only*/
        @IdRes textId: Int = 0,/*support TextView only*/
        @IdRes reloadClickId: Int = 0
    ) {
        emptyViewStub.layoutResource = layoutId
        _emptyIconId = iconId
        _emptyTextId = textId
        _emptyClickId = reloadClickId
        addView(emptyViewStub)
    }

    fun setErrorView(
        @LayoutRes layoutId: Int,
        @IdRes iconId: Int = 0,/*support ImageView only*/
        @IdRes textId: Int = 0,/*support TextView only*/
        @IdRes reloadClickId: Int = 0
    ) {
        errorViewStub.layoutResource = layoutId
        _errorIconId = iconId
        _errorTextId = textId
        _errorClickId = reloadClickId
        addView(errorViewStub)
    }

    fun setCustomLayout(
        @LayoutRes layoutId: Int,
        @IdRes reloadClickId: Int = 0
    ) {
        if (layoutId != 0) {
            customViewStub.layoutResource = layoutId
            _customClickId = reloadClickId
            addView(customViewStub)
            _hasCustom = true
        }
    }

    fun showLoading() {
        if (isShowing(_loadingView)) return
        if (hide(_emptyView)) {
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView)
        }
        if (hide(_errorView)) {
            _pageChangeListener?.onPageErrorChanged(false, _errorView)
        }
        if (_hasCustom && hide(_customView)) {
            _pageChangeListener?.onPageCustomChanged(false, _customView)
        }
        if (!inflatePage(State.LOADING)) {
            show(_loadingView)
        }
        _pageChangeListener?.onPageLoadingChanged(true, _loadingView)
    }

    fun showEmpty(message: String, @DrawableRes iconResId: Int) {
        if (isShowing(_emptyView)) return
        if (hide(_loadingView)) {
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView)
        }
        if (hide(_errorView)) {
            _pageChangeListener?.onPageErrorChanged(false, _errorView)
        }
        if (_hasCustom && hide(_customView)) {
            _pageChangeListener?.onPageCustomChanged(false, _customView)
        }
        if (!inflatePage(State.EMPTY)) {
            show(_emptyView)
        }
        setPage(State.EMPTY, message, iconResId)
        _pageChangeListener?.onPageEmptyChanged(true, _emptyView)
    }

    fun showError(message: String, @DrawableRes iconResId: Int) {
        if (isShowing(_errorView)) return
        if (hide(_loadingView)) {
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView)
        }
        if (hide(_emptyView)) {
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView)
        }
        if (_hasCustom && hide(_customView)) {
            _pageChangeListener?.onPageCustomChanged(false, _customView)
        }
        if (!inflatePage(State.ERROR)) {
            show(_errorView)
        }
        setPage(State.ERROR, message, iconResId)
        _pageChangeListener?.onPageErrorChanged(true, _errorView)
    }

    fun showCustom() {
        if (!_hasCustom || isShowing(_customView)) return
        if (hide(_loadingView)) {
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView)
        }
        if (hide(_emptyView)) {
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView)
        }
        if (hide(_errorView)) {
            _pageChangeListener?.onPageErrorChanged(false, _errorView)
        }
        if (!inflatePage(State.CUSTOM)) {
            show(_customView)
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

    override fun onClick(v: View?) {
        _reloadListener?.invoke()
    }

    private fun inflatePage(state: State): Boolean {
        when (state) {
            State.LOADING -> {
                return if (loadingViewStub.parent != null) {
                    if (loadingViewStub.layoutResource != 0) {
                        _loadingView = loadingViewStub.inflate()
                        _pageCreateListener?.onPageLoadingCreated(_loadingView)
                        true
                    } else {
                        throw IllegalArgumentException("Loading layout not set.")
                    }
                } else {
                    false
                }
            }
            State.EMPTY -> {
                return if (emptyViewStub.parent != null) {
                    if (emptyViewStub.layoutResource != 0) {
                        _emptyView = emptyViewStub.inflate()
                        invokeReload(_emptyView, _emptyClickId)
                        _pageCreateListener?.onPageEmptyCreated(_emptyView)
                        true
                    } else {
                        throw IllegalArgumentException("Empty layout not set.")
                    }
                } else {
                    false
                }
            }
            State.ERROR -> {
                return if (errorViewStub.parent != null) {
                    if (errorViewStub.layoutResource != 0) {
                        _errorView = errorViewStub.inflate()
                        invokeReload(_errorView, _errorClickId)
                        _pageCreateListener?.onPageErrorCreated(_errorView)
                        true
                    } else {
                        throw IllegalArgumentException("Error layout not set.")
                    }
                } else {
                    false
                }
            }
            State.CUSTOM -> {
                return if (customViewStub.parent != null) {
                    _customView = customViewStub.inflate()
                    invokeReload(_customView, _customClickId)
                    _pageCreateListener?.onPageCustomCreated(_customView)
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setPage(state: State, message: String, @DrawableRes iconResId: Int) {
        if (state == State.EMPTY) {
            setPageIcon(_emptyView, _emptyIconId, iconResId)
            setPageMessage(_emptyView, _emptyTextId, message)
        } else if (state == State.ERROR) {
            setPageIcon(_errorView, _errorIconId, iconResId)
            setPageMessage(_errorView, _errorTextId, message)
        }
    }

    private val views: SparseArray<View> = SparseArray()
    private fun getView(targetView: View?, @IdRes viewId: Int): View? {
        val view = views.get(viewId)
        if (view == null) {
            val new = targetView?.findViewById<View>(viewId)
            views.put(viewId, new)
            return new
        }
        return view
    }

    private fun setPageIcon(view: View?, @IdRes viewId: Int, @DrawableRes iconId: Int) {
        if (view != null && viewId != 0 && iconId != 0) {
            val imageView: View = view.findViewById(viewId)
            if (imageView is ImageView) {
                imageView.setImageResource(iconId)
            }
        }
    }

    private fun setPageMessage(view: View?, @IdRes viewId: Int, message: String) {
        if (view != null && viewId != 0 && message.isNotBlank()) {
            val textView: View = view.findViewById(viewId)
            if (textView is TextView) {
                textView.text = message
            }
        }
    }

    private fun invokeReload(view: View?, @IdRes viewId: Int) {
        if (view != null && viewId != 0) {
            view.findViewById<View>(viewId).setOnClickListener(this)
        }
    }

    private fun show(view: View?) {
        view?.visibility = VISIBLE
    }

    private fun hide(view: View?): Boolean =
        if (isShowing(view)) {
            view?.visibility = INVISIBLE
            true
        } else {
            false
        }

    private fun isShowing(view: View?): Boolean {
        return view?.visibility == VISIBLE
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _pageChangeListener = null
        _pageCreateListener = null
        _reloadListener = null
    }
}