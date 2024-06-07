package com.example.budgetbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TransactionAdapter(private val transactions: List<Transaction>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.findViewById(R.id.amount)
        val date: TextView = view.findViewById(R.id.date)
        val paymentMethod: TextView = view.findViewById(R.id.payment_method)
        val description: TextView = view.findViewById(R.id.description)
        val categoryId: TextView = view.findViewById(R.id.category_id)
        val type: TextView = view.findViewById(R.id.type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.amount.text = transaction.amount.toString()
        holder.date.text = transaction.date
        holder.paymentMethod.text = transaction.paymentMethod
        holder.description.text = transaction.description
        holder.categoryId.text = transaction.categoryId.toString()
        holder.type.text = transaction.type
    }

    override fun getItemCount() = transactions.size
}
