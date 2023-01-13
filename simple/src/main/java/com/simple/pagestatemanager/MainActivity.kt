package com.simple.pagestatemanager

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mminng.pagestate.PageStateManager

class MainActivity : AppCompatActivity() {

    private lateinit var pageStateManager: PageStateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pageStateManager = PageStateManager.Builder(this)
            .setEmptyLayout(R.layout.state_empty, R.id.empty_icon, R.id.empty_text, R.id.empty_refresh)
            .setErrorLayout(R.layout.state_error, R.id.error_icon, R.id.error_text, R.id.error_refresh)
            .setPageChangeListener {
                pageLoadingChanged { visible, view ->
                    Log.w("wtf", "LoadingVisible=$visible")
                }
                pageEmptyChanged { visible, view ->
                    Log.w("wtf", "EmptyVisible=$visible")
                }
                pageErrorChanged { visible, view ->
                    Log.w("wtf", "ErrorVisible=$visible")
                }
            }.build()

        pageStateManager.setReloadListener {
            Toast.makeText(this@MainActivity, "Reload", Toast.LENGTH_SHORT).show()
        }

        pageStateManager.showLoading()
        Handler().postDelayed({
            pageStateManager.showError()
//            Handler().postDelayed({
//                pageStateManager.showLoading()
//                Handler().postDelayed({
//                    pageStateManager.showEmpty()
//                }, 3000)
//            }, 3000)
        }, 3000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_page_state, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.loading -> {
                pageStateManager.showLoading()
            }
            R.id.content -> {
                pageStateManager.showContent()
            }
            R.id.empty -> {
                pageStateManager.showEmpty("EMPTY", R.drawable.ic_launcher_background)
            }
            R.id.error -> {
                pageStateManager.showError("ERROR", R.drawable.ic_launcher_background)
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }
}