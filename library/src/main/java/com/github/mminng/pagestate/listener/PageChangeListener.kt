package com.github.mminng.pagestate.listener

import android.view.View

class PageChangeListener : OnPageChangeListener {

    private var _pageLoadingChanged: ((visible: Boolean, view: View) -> Unit)? = null
    private var _pageEmptyChanged: ((visible: Boolean, view: View) -> Unit)? = null
    private var _pageErrorChanged: ((visible: Boolean, view: View) -> Unit)? = null
    private var _pageCustomChanged: ((visible: Boolean, view: View) -> Unit)? = null

    override fun onPageLoadingChanged(visible: Boolean, view: View?) {
        if (view != null) _pageLoadingChanged?.invoke(visible, view)
    }

    override fun onPageEmptyChanged(visible: Boolean, view: View?) {
        if (view != null) _pageEmptyChanged?.invoke(visible, view)
    }

    override fun onPageErrorChanged(visible: Boolean, view: View?) {
        if (view != null) _pageErrorChanged?.invoke(visible, view)
    }

    override fun onPageCustomChanged(visible: Boolean, view: View?) {
        if (view != null) _pageCustomChanged?.invoke(visible, view)
    }

    fun pageLoadingChanged(listener: (visible: Boolean, view: View) -> Unit) {
        _pageLoadingChanged = listener
    }

    fun pageEmptyChanged(listener: (visible: Boolean, view: View) -> Unit) {
        _pageEmptyChanged = listener
    }

    fun pageErrorChanged(listener: (visible: Boolean, view: View) -> Unit) {
        _pageErrorChanged = listener
    }

    fun pageCustomChanged(listener: (visible: Boolean, view: View) -> Unit) {
        _pageCustomChanged = listener
    }
}

internal interface OnPageChangeListener {

    fun onPageLoadingChanged(visible: Boolean, view: View?)

    fun onPageEmptyChanged(visible: Boolean, view: View?)

    fun onPageErrorChanged(visible: Boolean, view: View?)

    fun onPageCustomChanged(visible: Boolean, view: View?)
}