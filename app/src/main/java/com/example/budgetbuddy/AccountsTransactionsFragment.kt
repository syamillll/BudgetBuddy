package com.example.budgetbuddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AccountsTransactionsFragment : Fragment() {

    private lateinit var transactionsAdapter: TransactionAdapter
    private lateinit var transactions: List<TransactionDisplay>
    private lateinit var transactionDetails: List<Transaction>
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accounts_transactions, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewTransactions)
        recyclerView.layoutManager = LinearLayoutManager(context)

        databaseHelper = DatabaseHelper(requireContext())

        val paymentMethod = arguments?.getString("accountType")
        loadTransactions(paymentMethod)

        transactionsAdapter = TransactionAdapter(transactions)
        recyclerView.adapter = transactionsAdapter

        return view
    }

    private fun loadTransactions(paymentMethod: String?) {
        if (paymentMethod != null) {
            val accounts = databaseHelper.getAllAccounts()

            transactionDetails = databaseHelper.getTransactionsByPaymentMethod(paymentMethod)
            transactions = transactionDetails.map { transaction ->
                val category = databaseHelper.getCategoryById(transaction.categoryId)
                val account = accounts.firstOrNull { it.name == transaction.paymentMethod }

                TransactionDisplay(
                    id = transaction.id,
                    type = transaction.type,
                    amount = transaction.amount,
                    categoryIcon = category?.icon ?: "ic_no_image",
                    categoryName = category?.name ?: "Unknown",
                    accountIcon = account?.icon ?: "ic_no_image",
                    accountName = account?.name ?: "Unknown",
                    description = transaction.description,
                    date = transaction.date,
                )

            }
        } else {
            transactions = emptyList()
            transactionDetails = emptyList()
        }
    }
}
