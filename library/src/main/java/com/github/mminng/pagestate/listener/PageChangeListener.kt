package com.github.mminng.pagestate.listener

import android.view.View

/**
 * Created by zh on 2023/1/10.
 */
class PageChangeListener : OnPageChangeListener {

    private var _pageLoadingChanged: ((visible: Boolean, view: View) -> Unit)? = null
    private var _pageEmptyChanged: ((visible: Boolean, view: View) -> Unit)? = null
    private var _pageErrorChanged: ((visible: Boolean, view: View) -> Unit)? = null

    override fun onPageLoadingChanged(visible: Boolean, view: View) {
        _pageLoadingChanged?.invoke(visible, view)
    }

    override fun onPageEmptyChanged(visible: Boolean, view: View) {
        _pageEmptyChanged?.invoke(visible, view)
    }

    override fun onPageErrorChanged(visible: Boolean, view: View) {
        _pageErrorChanged?.invoke(visible, view)
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
}

interface OnPageChangeListener {

    fun onPageLoadingChanged(visible: Boolean, view: View)

    fun onPageEmptyChanged(visible: Boolean, view: View)

    fun onPageErrorChanged(visible: Boolean, view: View)
}