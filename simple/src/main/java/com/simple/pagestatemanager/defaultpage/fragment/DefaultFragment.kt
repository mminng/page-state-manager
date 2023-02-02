package com.simple.pagestatemanager.defaultpage.fragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.github.mminng.pagestate.PageStateManager
import com.simple.pagestatemanager.R

/**
 * Created by zh on 2023/1/15.
 */
class DefaultFragment : Fragment() {

    private lateinit var pageStateManager: PageStateManager
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
        val rootView: ViewGroup = requireView().parent as ViewGroup
        Log.e("wtf", "count2=${rootView.childCount}")
        pageStateManager = PageStateManager.Builder(this).build()
        pageStateManager.setReloadListener {
            showToast("Reload")
            load((1..3).random())
        }
        load()
        Handler().postDelayed({
            val root: ViewGroup = requireView().parent as ViewGroup
            Log.e("wtf", "count1=${root.childCount}")
        }, 0)
    }

    private fun load(showState: Int = 3) {
        pageStateManager.showLoading()
        Handler().postDelayed({
            when (showState) {
                1 -> {
                    pageStateManager.showContent()
                    showToast("Show content")
                    _loaded = true
                }
                2 -> pageStateManager.showEmpty()
                3 -> pageStateManager.showError()
            }
        }, 3000)
    }

    fun showLoading() {
        pageStateManager.showLoading()
        showToast("Show loading")
    }

    fun showContent() {
        pageStateManager.showContent()
        showToast("Show content")
        _loaded = true
    }

    fun showEmpty(message: String = "", @DrawableRes iconResId: Int = 0) {
        pageStateManager.showEmpty(message, iconResId)
        showToast("Show empty")
    }

    fun showError(message: String = "", @DrawableRes iconResId: Int = 0) {
        pageStateManager.showError(message, iconResId)
        showToast("Show error")
    }

    private fun showToast(message: String) {
        Toast.makeText(
            requireContext(),
            if (_loaded) "Content loaded" else message,
            Toast.LENGTH_SHORT
        ).show()
    }
}