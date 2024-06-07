package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)

        val btnAddCategory: Button = findViewById(R.id.btn_add_category)
        val btnAddTransaction: Button = findViewById(R.id.btn_add_transaction)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)

        btnAddCategory.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }

        btnAddTransaction.setOnClickListener {
            startActivity(Intent(this, AddTransactionActivity::class.java))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapters
        categoryAdapter = CategoryAdapter(databaseHelper.getAllCategories())
        transactionAdapter = TransactionAdapter(databaseHelper.getAllTransactions())

        recyclerView.adapter = categoryAdapter // or transactionAdapter based on requirement
    }
}
