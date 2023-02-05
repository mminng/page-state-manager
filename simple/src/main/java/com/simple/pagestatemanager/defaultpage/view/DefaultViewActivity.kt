package com.simple.pagestatemanager.defaultpage.view

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mminng.pagestate.PageStateManager
import com.simple.pagestatemanager.R

class DefaultViewActivity : AppCompatActivity() {

    private lateinit var pageManager1: PageStateManager
    private lateinit var pageManager2: PageStateManager
    private lateinit var pageManager3: PageStateManager
    private lateinit var pageManager4: PageStateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_default)
        val actionBar = supportActionBar
        actionBar?.let {
            actionBar.title = "On View"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        pageManager1 = PageStateManager.Builder(findViewById(R.id.view_one))
            .setLoadingLayout(R.layout.page_state_loading)
            .setEmptyLayout(
                layoutId = R.layout.page_state_empty,
                clickId = R.id.state_empty_btn
            )
            .setErrorLayout(
                layoutId = R.layout.page_state_error,
                clickId = R.id.state_error_btn
            ).build()
        pageManager2 = PageStateManager.Builder(findViewById(R.id.view_two))
            .setLoadingLayout(R.layout.page_state_loading)
            .setEmptyLayout(
                layoutId = R.layout.page_state_empty,
                clickId = R.id.state_empty_btn
            )
            .setErrorLayout(
                layoutId = R.layout.page_state_error,
                clickId = R.id.state_error_btn
            ).build()
        pageManager3 = PageStateManager.Builder(findViewById(R.id.view_three))
            .setLoadingLayout(R.layout.page_state_loading)
            .setEmptyLayout(
                layoutId = R.layout.page_state_empty,
                clickId = R.id.state_empty_btn
            )
            .setErrorLayout(
                layoutId = R.layout.page_state_error,
                clickId = R.id.state_error_btn
            ).build()
        pageManager4 = PageStateManager.Builder(findViewById(R.id.view_four))
            .setLoadingLayout(R.layout.page_state_loading)
            .setEmptyLayout(
                layoutId = R.layout.page_state_empty,
                clickId = R.id.state_empty_btn
            )
            .setErrorLayout(
                layoutId = R.layout.page_state_error,
                clickId = R.id.state_error_btn
            ).build()
        pageManager1.setReloadListener {
            load1((1..3).random())
        }
        pageManager2.setReloadListener {
            load2((1..3).random())
        }
        pageManager3.setReloadListener {
            load3((1..3).random())
        }
        pageManager4.setReloadListener {
            load4((1..3).random())
        }
        load1((1..3).random())
        load2((1..3).random())
        load3((1..3).random())
        load4((1..3).random())
    }

    private fun load1(showState: Int) {
        pageManager1.showLoading()
        Handler().postDelayed({
            when (showState) {
                1 -> pageManager1.showContent()
                2 -> pageManager1.showEmpty()
                3 -> pageManager1.showError()
            }
        }, 3000)
    }

    private fun load2(showState: Int) {
        pageManager2.showLoading()
        Handler().postDelayed({
            when (showState) {
                1 -> pageManager2.showContent()
                2 -> pageManager2.showEmpty()
                3 -> pageManager2.showError()
            }
        }, 3000)
    }

    private fun load3(showState: Int) {
        pageManager3.showLoading()
        Handler().postDelayed({
            when (showState) {
                1 -> pageManager3.showContent()
                2 -> pageManager3.showEmpty()
                3 -> pageManager3.showError()
            }
        }, 3000)
    }

    private fun load4(showState: Int) {
        pageManager4.showLoading()
        Handler().postDelayed({
            when (showState) {
                1 -> pageManager4.showContent()
                2 -> pageManager4.showEmpty()
                3 -> pageManager4.showError()
            }
        }, 3000)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}