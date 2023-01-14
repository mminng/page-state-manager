package com.github.mminng.pagestate.listener

import android.view.View

/**
 * Created by zh on 2023/1/10.
 */
class PageCreateListener : OnPageCreateListener {

    private var _pageLoadingCreated: ((view: View) -> Unit)? = null
    private var _pageEmptyCreated: ((view: View) -> Unit)? = null
    private var _pageErrorCreated: ((view: View) -> Unit)? = null
    private var _pageCustomCreated: ((view: View) -> Unit)? = null

    override fun onPageLoadingCreated(view: View?) {
        view?.let { _pageLoadingCreated?.invoke(it) }
    }

    override fun onPageEmptyCreated(view: View?) {
        view?.let { _pageEmptyCreated?.invoke(it) }
    }

    override fun onPageErrorCreated(view: View?) {
        view?.let { _pageErrorCreated?.invoke(it) }
    }

    override fun onPageCustomCreated(view: View?) {
        view?.let { _pageCustomCreated?.invoke(view) }
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

    fun pageCustomCreated(listener: (view: View) -> Unit) {
        _pageCustomCreated = listener
    }
}

interface OnPageCreateListener {

    fun onPageLoadingCreated(view: View?)

    fun onPageEmptyCreated(view: View?)

    fun onPageErrorCreated(view: View?)

    fun onPageCustomCreated(view: View?)
}