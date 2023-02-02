package com.simple.pagestatemanager.defaultpage.activity

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mminng.pagestate.PageStateManager
import com.simple.pagestatemanager.R

class DefaultActivity : AppCompatActivity() {

    private lateinit var pageManager: PageStateManager
    private var _loaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)
        val actionBar = supportActionBar
        actionBar?.let {
            actionBar.title = "On Activity"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.loading -> {
                showToast()
                pageManager.showLoading()
            }
            R.id.content -> {
                showToast()
                pageManager.showContent()
                _loaded = true
            }
            R.id.empty -> {
                showToast()
                pageManager.showEmpty()
            }
            R.id.error -> {
                showToast()
                pageManager.showError()
            }
            R.id.empty_change -> {
                showToast()
                pageManager.showEmpty("Okay...", R.drawable.ic_empty)
            }
            R.id.error_change -> {
                showToast()
                pageManager.showError("Oh no!", R.drawable.ic_error)
            }
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_page_state, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showToast() {
        if (_loaded) {
            Toast.makeText(this, "Content loaded", Toast.LENGTH_SHORT).show()
        }
    }
}