package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

        // Initialize adapters
        categoryAdapter = CategoryAdapter(databaseHelper.getAllCategories())
        transactionAdapter = TransactionAdapter(databaseHelper.getAllTransactions())

        recyclerView.adapter = categoryAdapter // or transactionAdapter based on requirement

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.navigation_records -> selectedFragment = RecordsFragment()
                R.id.navigation_analysis -> selectedFragment = AnalysisFragment()
                R.id.navigation_budgets -> selectedFragment = BudgetsFragment()
                R.id.navigation_accounts -> selectedFragment = AccountsFragment()
                R.id.navigation_categories -> selectedFragment = CategoriesFragment()
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit()
                Log.d("MainActivity", "Selected fragment: ${selectedFragment.javaClass.simpleName}") // Add this line
            }
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.navigation_records
    }
}

