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

    private lateinit var pageStateManager: PageStateManager
    private var _loaded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_default)
        val actionBar = supportActionBar
        actionBar?.let {
            actionBar.title = "Default(On Activity)"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        pageStateManager = PageStateManager.Builder(this).build()
        pageStateManager.setReloadListener {
            showToast("Reload")
            load((1..3).random())
        }

        load()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.loading -> {
                pageStateManager.showLoading()
                showToast("Show loading")
            }
            R.id.content -> {
                pageStateManager.showContent()
                showToast("Show content")
                _loaded = true
            }
            R.id.empty -> {
                pageStateManager.showEmpty()
                showToast("Show empty")
            }
            R.id.error -> {
                pageStateManager.showError()
                showToast("Show error")
            }
            R.id.empty_change -> {
                pageStateManager.showEmpty("Okay...", R.drawable.ic_empty)
                showToast("Show empty")
            }
            R.id.error_change -> {
                pageStateManager.showError("Oh no!", R.drawable.ic_error)
                showToast("Show error")
            }
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_default_page_state, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, if (_loaded) "Content loaded" else message, Toast.LENGTH_SHORT).show()
    }
}