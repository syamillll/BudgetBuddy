// TransactionsActivity.kt
package com.example.budgetbuddy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TransactionsActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transactions)

        db = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewTransactions)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadTransactions()
    }

    private fun loadTransactions() {
        val transactions = db.getAllTransactions()
        val accounts = db.getAllAccounts()
        val categories = db.getAllCategories()

        val transactionDisplays = transactions.map { transaction ->
            val category = categories.firstOrNull { it.id == transaction.categoryId }
            val account = accounts.firstOrNull { it.name == transaction.paymentMethod }

            TransactionDisplay(
                categoryIcon = category?.icon ?: "ic_default_category",
                categoryName = category?.name ?: "Unknown",
                accountName = account?.name ?: "Unknown",
                accountIcon = account?.icon ?: "ic_default_account",
                amount = transaction.amount
            )
        }

        adapter = TransactionAdapter(transactionDisplays, transactions)
        recyclerView.adapter = adapter
    }
}
