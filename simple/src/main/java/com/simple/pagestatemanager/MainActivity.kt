package com.simple.pagestatemanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.simple.pagestatemanager.activity.DefaultActivity
import com.simple.pagestatemanager.fragment.DefaultFragmentActivity
import com.simple.pagestatemanager.fragment.ViewPager2FragmentActivity
import com.simple.pagestatemanager.lottie.LottieActivity
import com.simple.pagestatemanager.view.DefaultViewActivity

/**
 * Created by zh on 2023/1/15.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.open_activity).setOnClickListener {
            startActivity(Intent(this@MainActivity, DefaultActivity::class.java))
        }
        findViewById<View>(R.id.open_fragment).setOnClickListener {
            startActivity(Intent(this@MainActivity, DefaultFragmentActivity::class.java))
        }
        findViewById<View>(R.id.open_viewpager2).setOnClickListener {
            startActivity(Intent(this@MainActivity, ViewPager2FragmentActivity::class.java))
        }
        findViewById<View>(R.id.open_view).setOnClickListener {
            startActivity(Intent(this@MainActivity, DefaultViewActivity::class.java))
        }
        findViewById<View>(R.id.open_lottie).setOnClickListener {
            startActivity(Intent(this@MainActivity, LottieActivity::class.java))
        }
    }
}