package com.simple.pagestatemanager.defaultpage.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.github.mminng.pagestate.PageStateManager
import com.simple.pagestatemanager.R

/**
 * Created by zh on 2023/1/15.
 */
class DefaultFragment : Fragment() {

    private lateinit var pageManager: PageStateManager
    private var _loaded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_default, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pageManager = PageStateManager.Builder(this)
            .setLoadingLayout(R.layout.page_state_loading)
            .setEmptyLayout(
                layoutId = R.layout.page_state_empty,
                iconId = R.id.state_empty_icon,
                textId = R.id.state_empty_text,
                clickId = R.id.state_empty_btn
            )
            .setErrorLayout(
                layoutId = R.layout.page_state_error,
                iconId = R.id.state_error_icon,
                textId = R.id.state_error_text,
                clickId = R.id.state_error_btn
            ).build()
        pageManager.setReloadListener {
            load((1..3).random())
        }
        load()
    }

    private fun load(showState: Int = 3) {
        pageManager.showLoading()
        Handler().postDelayed({
            when (showState) {
                1 -> {
                    pageManager.showContent()
                    _loaded = true
                }
                2 -> pageManager.showEmpty()
                3 -> pageManager.showError()
            }
        }, 3000)
    }

    fun showLoading() {
        showToast()
        pageManager.showLoading()
    }

    fun showContent() {
        showToast()
        pageManager.showContent()
        _loaded = true
    }

    fun showEmpty(message: String = "", @DrawableRes iconResId: Int = 0) {
        showToast()
        pageManager.showEmpty(message, iconResId)
    }

    fun showError(message: String = "", @DrawableRes iconResId: Int = 0) {
        showToast()
        pageManager.showError(message, iconResId)
    }

    private fun showToast() {
        if (_loaded) {
            Toast.makeText(requireContext(), "Content loaded", Toast.LENGTH_SHORT).show()
        }
    }
}