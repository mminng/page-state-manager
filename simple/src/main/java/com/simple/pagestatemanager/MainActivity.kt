package com.simple.pagestatemanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simple.pagestatemanager.defaultpage.activity.DefaultActivity
import com.simple.pagestatemanager.defaultpage.fragment.DefaultFragmentActivity

/**
 * Created by zh on 2023/1/15.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.open_default_activity).setOnClickListener {
            startActivity(Intent(this@MainActivity, DefaultActivity::class.java))
        }
        findViewById<View>(R.id.open_default_fragment).setOnClickListener {
            startActivity(Intent(this@MainActivity, DefaultFragmentActivity::class.java))
        }
        findViewById<View>(R.id.open_custom).setOnClickListener { }
    }
}