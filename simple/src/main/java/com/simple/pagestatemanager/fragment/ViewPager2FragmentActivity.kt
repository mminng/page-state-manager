package com.simple.pagestatemanager.fragment

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.simple.pagestatemanager.R

class ViewPager2FragmentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_viewpager2)
        val viewpager: ViewPager2 = findViewById(R.id.viewpager2)
        val tabLayout: TabLayout = findViewById(R.id.tablayout)
        val actionBar = supportActionBar
        actionBar?.let {
            actionBar.title = "On Fragment With ViewPager2"
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val fragments: List<Fragment> = listOf(
            ViewPager2Fragment(),
            ViewPager2Fragment(),
            ViewPager2Fragment(),
            ViewPager2Fragment(),
        )

        val pagerAdapter: FragmentStateAdapter =
            object : FragmentStateAdapter(this@ViewPager2FragmentActivity) {
                override fun getItemCount(): Int = fragments.size
                override fun createFragment(position: Int): Fragment {
                    return fragments[position]
                }
            }

        viewpager.adapter = pagerAdapter
        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> "One"
                1 -> "Two"
                2 -> "Three"
                3 -> "Four"
                else -> ""
            }
        }.attach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}