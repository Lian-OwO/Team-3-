package com.example.firebasetest.project3_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasetest.project3_android.Adapter.CategoryAdapter
import com.example.firebasetest.project3_android.MainFragment.ClassifierFragment
import com.example.firebasetest.project3_android.MainFragment.HomeFragment
import com.example.firebasetest.project3_android.MainFragment.MapFragment
import com.example.firebasetest.project3_android.MainFragment.NoticeFragment
import com.example.firebasetest.project3_android.MainFragment.ReserveResultFragment
import com.example.firebasetest.project3_android.Model.CategoryItem
import com.example.firebasetest.project3_android.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBindingbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBindingbinding = ActivityMainBinding.inflate(layoutInflater)
        val tabLayout = activityMainBindingbinding.tabs
        setContentView(activityMainBindingbinding.root)

        setSupportActionBar(activityMainBindingbinding.toolbar)

        // HomeFragment를 초기 화면으로 설정
        val homeFragment = HomeFragment()
        supportFragmentManager.beginTransaction().replace(R.id.tabContent, homeFragment).commit()

        initializeRecyclerView()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                when (tab?.text) {
                    "홈" -> {
                        transaction.replace(R.id.tabContent, HomeFragment())
                        activityMainBindingbinding.header.visibility = View.VISIBLE
                    }
                    "예약" -> {
                        transaction.replace(R.id.tabContent, ReserveResultFragment())
                        activityMainBindingbinding.header.visibility = View.VISIBLE
                    }
                    "맵" -> {
                        transaction.replace(R.id.tabContent, MapFragment())
                        activityMainBindingbinding.header.visibility = View.VISIBLE
                    }
                    "공지" -> {
                        transaction.replace(R.id.tabContent, NoticeFragment())
                        activityMainBindingbinding.header.visibility = View.VISIBLE
                    }
                    "Ai" -> {
                        transaction.replace(R.id.tabContent, ClassifierFragment())
                        activityMainBindingbinding.header.visibility = View.VISIBLE
                    }
                }
                transaction.commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Toast.makeText(this@MainActivity, "onTabUnselected", Toast.LENGTH_SHORT).show()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                Toast.makeText(this@MainActivity, "onTabReselected", Toast.LENGTH_SHORT).show()
            }
        })

        // 화면 다시 그리기
        invalidateOptionsMenu()
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    private fun initializeRecyclerView() {
        val categoryRecyclerView: RecyclerView = activityMainBindingbinding.categoryRecyclerView
        categoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val categoryItems = listOf(
            CategoryItem(R.drawable.category, "서천 갯벌"),
            CategoryItem(R.drawable.category, "고창 갯벌"),
            CategoryItem(R.drawable.category, "신안 갯벌"),
            CategoryItem(R.drawable.category, "보성-순천 갯벌"),
//            CategoryItem(R.drawable.category, "선상낚시"),
//            CategoryItem(R.drawable.category, "좌대낚시"),
//            CategoryItem(R.drawable.category, "갯바위낚시"),
//            CategoryItem(R.drawable.category, "방파제낚시"),
//            CategoryItem(R.drawable.category, "양어장"),
//            CategoryItem(R.drawable.category, "연안좌대"),
//            CategoryItem(R.drawable.category, "수상좌대"),
//            CategoryItem(R.drawable.category, "민물포인트"),
        )

        val categoryAdapter = CategoryAdapter(categoryItems)
        categoryRecyclerView.adapter = categoryAdapter
    }


}