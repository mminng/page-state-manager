package com.github.mminng.pagestate.listener

import android.view.View

/**
 * Created by zh on 2023/1/10.
 */
class PageCreateListener : OnPageCreateListener {

    private var _pageLoadingCreated: ((view: View) -> Unit)? = null
    private var _pageEmptyCreated: ((view: View) -> Unit)? = null
    private var _pageErrorCreated: ((view: View) -> Unit)? = null

    override fun onPageLoadingCreated(view: View) {
        _pageLoadingCreated?.invoke(view)
    }

    override fun onPageEmptyCreated(view: View) {
        _pageEmptyCreated?.invoke(view)
    }

    override fun onPageErrorCreated(view: View) {
        _pageErrorCreated?.invoke(view)
    }

    fun pageLoadingCreated(listener: (view: View) -> Unit) {
        _pageLoadingCreated = listener
    }

    fun pageEmptyCreated(listener: (view: View) -> Unit) {
        _pageEmptyCreated = listener
    }

    fun pageErrorCreated(listener: (view: View) -> Unit) {
        _pageErrorCreated = listener
    }
}

interface OnPageCreateListener {

    fun onPageLoadingCreated(view: View)

    fun onPageEmptyCreated(view: View)

    fun onPageErrorCreated(view: View)
}