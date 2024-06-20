package com.example.budgetbuddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AccountsFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var accountsAdapter: AccountsAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_accounts, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewAccounts)
        recyclerView.layoutManager = LinearLayoutManager(context)
        databaseHelper = DatabaseHelper(requireContext())

        loadAccounts()

        return view
    }

    private fun loadAccounts() {
        val accounts = databaseHelper.getAllAccounts()
        accountsAdapter = AccountsAdapter(accounts)
        recyclerView.adapter = accountsAdapter
    }

}

