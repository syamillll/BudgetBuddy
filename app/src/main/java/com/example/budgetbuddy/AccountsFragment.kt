package com.example.budgetbuddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AccountsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var accountsAdapter: AccountsAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var expenseAmount: TextView
    private lateinit var incomeAmount: TextView
    private lateinit var allAccountsBalance: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accounts, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewAccounts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        databaseHelper = DatabaseHelper(requireContext())

        expenseAmount = view.findViewById(R.id.expense_amount)
        incomeAmount = view.findViewById(R.id.income_amount)
        allAccountsBalance = view.findViewById(R.id.all_accounts_balance)

        loadAccounts()
        loadOverallBalance()

        return view
    }

    private fun loadAccounts() {
        val accounts = databaseHelper.getAllAccounts()
        val transactions = databaseHelper.getAllTransactions()

        val cashTransactions = transactions.filter { it.paymentMethod == "Cash" }
        val cardTransactions = transactions.filter { it.paymentMethod == "Card" }

        val totalCashTransactions = cashTransactions.sumByDouble { if (it.type.equals("Income", ignoreCase = true)) it.amount.toDouble() else -it.amount.toDouble() }.toFloat()
        val totalCardTransactions = cardTransactions.sumByDouble { if (it.type.equals("Income", ignoreCase = true)) it.amount.toDouble() else -it.amount.toDouble() }.toFloat()

        val updatedAccounts = accounts.map {
            when (it.name) {
                "Cash" -> it.copy(balance = totalCashTransactions)
                "Card" -> it.copy(balance = totalCardTransactions)
                else -> it
            }
        }

        accountsAdapter = AccountsAdapter(updatedAccounts) { account ->
            // Handle account click here
            val fragment = TransactionsFragment().apply {
                arguments = Bundle().apply {
                    putString("accountType", account.name)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = accountsAdapter
    }

    private fun loadOverallBalance() {
        val transactions = databaseHelper.getAllTransactions()
        var totalExpense = 0f
        var totalIncome = 0f

        for (transaction in transactions) {
            if (transaction.type.equals("Expense", ignoreCase = true)) {
                totalExpense += transaction.amount
            } else if (transaction.type.equals("Income", ignoreCase = true)) {
                totalIncome += transaction.amount
            }
        }

        expenseAmount.text = "-RM${String.format("%.2f", totalExpense)}"
        incomeAmount.text = "RM${String.format("%.2f", totalIncome)}"
        val totalBalance = totalIncome - totalExpense
        allAccountsBalance.text = "All accounts balance: RM${String.format("%.2f", totalBalance)}"
    }
}
