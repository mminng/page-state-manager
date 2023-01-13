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
    private var _contentView: View? = null
    private var _loadingView: View? = null
    private var _emptyView: View? = null
    private var _errorView: View? = null

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

    fun showLoading() {
        if (contains(_loadingView) || _done) return

        val emptyView: Pair<Boolean, Int> = containsAndPosition(_emptyView)
        if (emptyView.first) {
            removeViewAt(emptyView.second)
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView!!)
        }

        val errorView: Pair<Boolean, Int> = containsAndPosition(_errorView)
        if (errorView.first) {
            removeViewAt(errorView.second)
            _pageChangeListener?.onPageErrorChanged(false, _errorView!!)
        }

        if (loadingViewStub.parent != null) {
            _loadingView = loadingViewStub.inflate()
            _pageCreateListener?.onPageLoadingCreated(_loadingView!!)
        } else {
            addView(_loadingView)
        }
        _pageChangeListener?.onPageLoadingChanged(true, _loadingView!!)
    }

    fun showContent() {
        if (_done) return

        val loadingView: Pair<Boolean, Int> = containsAndPosition(_loadingView)
        if (loadingView.first) {
            removeViewAt(loadingView.second)
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView!!)
        }

        val emptyView: Pair<Boolean, Int> = containsAndPosition(_emptyView)
        if (emptyView.first) {
            removeViewAt(emptyView.second)
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView!!)
        }

        val errorView: Pair<Boolean, Int> = containsAndPosition(_errorView)
        if (errorView.first) {
            removeViewAt(errorView.second)
            _pageChangeListener?.onPageErrorChanged(false, _errorView!!)
        }

        addView(_contentView)
        _done = true
        _pageChangeListener = null
        _pageCreateListener = null
        _reloadListener = null
    }

    fun showEmpty(message: String, @DrawableRes iconResId: Int) {
        if (contains(_emptyView) || _done) return

        val loadingView: Pair<Boolean, Int> = containsAndPosition(_loadingView)
        if (loadingView.first) {
            removeViewAt(loadingView.second)
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView!!)
        }

        val errorView: Pair<Boolean, Int> = containsAndPosition(_errorView)
        if (errorView.first) {
            removeViewAt(errorView.second)
            _pageChangeListener?.onPageErrorChanged(false, _errorView!!)
        }

        if (emptyViewStub.parent != null) {
            _emptyView = emptyViewStub.inflate()
            if (_emptyLayoutId == DEFAULT_EMPTY_LAYOUT) {
                invokeReload(_emptyView!!, R.id.state_empty_btn)
            } else {
                invokeReload(_emptyView!!, _emptyClickId)
            }
            _pageCreateListener?.onPageEmptyCreated(_emptyView!!)
        } else {
            addView(_emptyView)
        }
        if (_emptyLayoutId == DEFAULT_EMPTY_LAYOUT) {
            setPageIcon(_emptyView!!, R.id.state_empty_icon, iconResId)
            setPageMessage(_emptyView!!, R.id.state_empty_text, message)
        } else {
            setPageIcon(_emptyView!!, _emptyIconId, iconResId)
            setPageMessage(_emptyView!!, _emptyTextId, message)
        }
        _pageChangeListener?.onPageEmptyChanged(true, _emptyView!!)
    }

    fun showError(message: String, @DrawableRes iconResId: Int) {
        if (contains(_errorView) || _done) return

        val loadingView: Pair<Boolean, Int> = containsAndPosition(_loadingView)
        if (loadingView.first) {
            removeViewAt(loadingView.second)
            _pageChangeListener?.onPageLoadingChanged(false, _loadingView!!)
        }

        val emptyView: Pair<Boolean, Int> = containsAndPosition(_emptyView)
        if (emptyView.first) {
            removeViewAt(emptyView.second)
            _pageChangeListener?.onPageEmptyChanged(false, _emptyView!!)
        }

        if (errorViewStub.parent != null) {
            _errorView = errorViewStub.inflate()
            if (_errorLayoutId == DEFAULT_ERROR_LAYOUT) {
                invokeReload(_errorView!!, R.id.state_error_btn)
            } else {
                invokeReload(_errorView!!, _errorClickId)
            }
            _pageCreateListener?.onPageErrorCreated(_errorView!!)
        } else {
            addView(_errorView)
        }
        if (_errorLayoutId == DEFAULT_EMPTY_LAYOUT) {
            setPageIcon(_errorView!!, R.id.state_error_icon, iconResId)
            setPageMessage(_errorView!!, R.id.state_error_text, message)
        } else {
            setPageIcon(_errorView!!, _errorIconId, iconResId)
            setPageMessage(_errorView!!, _errorTextId, message)
        }
        _pageChangeListener?.onPageErrorChanged(true, _errorView!!)
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

    private fun contains(view: View?): Boolean {
        if (view == null) return false
        return indexOfChild(view) >= 0
    }

    private fun containsAndPosition(view: View?): Pair<Boolean, Int> {
        if (view == null) return Pair.create(false, -1)
        val index: Int = indexOfChild(view)
        return Pair.create(index >= 0, index)
    }

    private fun setPageIcon(view: View, @IdRes viewId: Int, @DrawableRes iconId: Int) {
        if (viewId > 0 && iconId > 0) {
            val imageView: View = view.findViewById(viewId)
            if (imageView is ImageView) {
                imageView.setImageResource(iconId)
            }
        }
    }

    private fun setPageMessage(view: View, @IdRes viewId: Int, message: String) {
        if (viewId > 0 && message.isNotEmpty()) {
            val textView: View = view.findViewById(viewId)
            if (textView is TextView) {
                textView.text = message
            }
        }
    }

    private fun invokeReload(view: View, @IdRes viewId: Int) {
        if (viewId > 0) {
            view.findViewById<View>(viewId).setOnClickListener {
                _reloadListener?.invoke()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        _pageChangeListener = null
        _pageCreateListener = null
        _reloadListener = null
    }
}