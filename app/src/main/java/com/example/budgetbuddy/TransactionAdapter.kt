package com.example.budgetbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

data class TransactionDisplay(
    val categoryIcon: String,
    val categoryName: String,
    val accountName: String,
    val accountIcon: String,
    val type: String,
    val amount: Float
)

class TransactionAdapter(
    private val transactions: List<TransactionDisplay>,
    private val transactionDetails: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
        private val categoryName: TextView = itemView.findViewById(R.id.categoryName)
        private val accountIcon: ImageView = itemView.findViewById(R.id.accountIcon)
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
            val accountIconResId = context.resources.getIdentifier(transaction.accountIcon, "drawable", context.packageName)
            if (accountIconResId != 0) {
                accountIcon.setImageResource(accountIconResId)
            } else {
                accountIcon.setImageResource(R.drawable.ic_no_image) // Default account icon
            }
            categoryName.text = transaction.categoryName
            accountName.text = transaction.accountName

            // Set text color and prefix based on transaction type
            if (transaction.type.equals("Income", ignoreCase = true)) {
                val amountWithSymbol = "+ RM${transaction.amount.toString()}"
                transactionAmount.text = amountWithSymbol
                transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.emerald)) // Assuming you have defined a green color resource
            } else {
                val amountWithSymbol = "- RM${transaction.amount.toString()}"
                transactionAmount.text = amountWithSymbol
                transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.light_red)) // Assuming you have defined a red color resource
            }
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

    @SuppressLint("SetTextI18n")
    private fun showTransactionDetails(context: Context, transaction: Transaction) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.transaction_details_modal, null)
        val dialogBuilder = MaterialAlertDialogBuilder(context).setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val transactionDetailsTextView: TextView = dialogView.findViewById(R.id.transaction_details)
        transactionDetailsTextView.text = """
            ID: ${transaction.id}
            Category ID: ${transaction.categoryId}
            Amount: ${transaction.amount}
            Date: ${transaction.date}
            Payment Method: ${transaction.paymentMethod}
            Description: ${transaction.description}
            Type: ${transaction.type}
        """.trimIndent()

        val btnClose: ImageButton = dialogView.findViewById(R.id.btn_close)
        val btnEdit: ImageButton = dialogView.findViewById(R.id.btn_edit)
        val btnDelete: ImageButton = dialogView.findViewById(R.id.btn_delete)

        btnClose.setOnClickListener {
            alertDialog.dismiss()
        }

        btnEdit.setOnClickListener {
            val intent = Intent(context, EditTransactionActivity::class.java)
            intent.putExtra("transaction_id", transaction.id)
            context.startActivity(intent)
            alertDialog.dismiss()
        }

        btnDelete.setOnClickListener {
            deleteTransaction(context, transaction)
            alertDialog.dismiss()
        }
    }

    private fun deleteTransaction(context: Context, transaction: Transaction) {
        val databaseHelper = DatabaseHelper(context)
        databaseHelper.deleteTransaction(transaction.id)
        (context as? MainActivity)?.loadTransactions() // Refresh the list after deletion
    }
}
