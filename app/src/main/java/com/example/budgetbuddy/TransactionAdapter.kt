package com.example.budgetbuddy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

data class TransactionDisplay(
    val categoryIcon: String,
    val categoryName: String,
    val accountName: String,
    val accountIcon: String,
    val amount: Float
)

class TransactionAdapter(
    private val transactions: List<TransactionDisplay>,
    private val transactionDetails: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
        private val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        private val accountName: TextView = itemView.findViewById(R.id.accountName)
        private val transactionAmount: TextView = itemView.findViewById(R.id.transactionAmount)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val transaction = transactionDetails[position]
                    showTransactionDetails(itemView.context, transaction)
                }
            }
        }

        fun bind(transaction: TransactionDisplay) {
            val context = itemView.context
            val categoryIconResId = context.resources.getIdentifier(transaction.categoryIcon, "drawable", context.packageName)
            if (categoryIconResId != 0) {
                categoryIcon.setImageResource(categoryIconResId)
            } else {
                categoryIcon.setImageResource(R.drawable.ic_no_image) // Default icon if not found
            }
            categoryName.text = transaction.categoryName
            accountName.text = transaction.accountName
            transactionAmount.text = transaction.amount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    override fun getItemCount() = transactions.size

    private fun showTransactionDetails(context: Context, transaction: Transaction) {
        val details = """
            ID: ${transaction.id}
            Category ID: ${transaction.categoryId}
            Amount: ${transaction.amount}
            Date: ${transaction.date}
            Payment Method: ${transaction.paymentMethod}
            Description: ${transaction.description}
            Type: ${transaction.type}
        """.trimIndent()

        MaterialAlertDialogBuilder(context)
            .setTitle("Transaction Details")
            .setMessage(details)
            .setPositiveButton("OK", null)
            .show()
    }
}
