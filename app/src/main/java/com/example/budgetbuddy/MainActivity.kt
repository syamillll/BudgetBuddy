package com.example.budgetbuddy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        recyclerView.layoutManager = LinearLayoutManager(this)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.button_color)
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_records -> selectedFragment = TransactionsFragment()
                R.id.navigation_analysis -> selectedFragment = AnalysisFragment()
                R.id.navigation_budgets -> selectedFragment = BudgetsFragment()
                R.id.navigation_accounts -> selectedFragment = AccountsFragment()
                R.id.navigation_categories -> selectedFragment = CategoriesFragment()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                Log.d("MainActivity", "Selected fragment: ${selectedFragment.javaClass.simpleName}")
            }
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.navigation_records
    }

    fun loadTransactions() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as? TransactionsFragment
        fragment?.loadTransactions()
    }
}
