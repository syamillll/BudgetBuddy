package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecordsFragment : Fragment() {
    private lateinit var db: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_records, container, false)

        // Initialize the database helper
        db = DatabaseHelper(requireContext())

        // Set up the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewTransactions)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Load transactions and set up the adapter
        loadTransactions()

        // Set up the FloatingActionButton
        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            // Create an Intent to start AddTransactionActivity
            val intent = Intent(activity, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    // Get transactions
    fun loadTransactions() {
        val transactions = db.getAllTransactions()
        val accounts = db.getAllAccounts()
        val categories = db.getAllCategories()

        val transactionDisplays = transactions.map { transaction ->
            val category = categories.firstOrNull { it.id == transaction.categoryId }
            val account = accounts.firstOrNull { it.name == transaction.paymentMethod }

            TransactionDisplay(
                categoryIcon = category?.icon ?: "ic_no_image",
                categoryName = category?.name ?: "Unknown",
                accountName = account?.name ?: "Unknown",
                accountIcon = account?.icon ?: "ic_no_image",
                type = transaction.type,
                amount = transaction.amount
            )
        }

        adapter = TransactionAdapter(transactionDisplays, transactions)
        recyclerView.adapter = adapter
    }

    // Reload transactions on page refresh
    override fun onResume() {
        super.onResume()
        loadTransactions()
    }
}
