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
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Date

data class TransactionDisplay(
    val id: Int,
    val type: String,
    val amount: Float,
    val categoryIcon: String,
    val categoryName: String,
    val accountIcon: String,
    val accountName: String,
    val description: String,
    val date: String,
)

class TransactionAdapter(
    private var transactions: List<TransactionDisplay>,
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
                    val transaction = transactions[position]
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
                val amountWithSymbol = "+ RM${transaction.amount}"
                transactionAmount.text = amountWithSymbol
                transactionAmount.setTextColor(ContextCompat.getColor(context, R.color.emerald)) // Assuming you have defined a green color resource
            } else {
                val amountWithSymbol = "- RM${transaction.amount}"
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
    private fun showTransactionDetails(context: Context, transaction: TransactionDisplay) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.transaction_details_modal, null)
        val dialogBuilder = MaterialAlertDialogBuilder(context).setView(dialogView)
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // Set transaction details
        val btnContainer: LinearLayout = dialogView.findViewById(R.id.btn_container)
        val amountContainer: LinearLayout = dialogView.findViewById(R.id.amount_container)
        val transactionTypeTextView: TextView = dialogView.findViewById(R.id.transaction_type)
        val transactionAmountTextView: TextView = dialogView.findViewById(R.id.transaction_amount)
        val categoryIconImageView: ImageView = dialogView.findViewById(R.id.category_icon)
        val categoryNameTextView: TextView = dialogView.findViewById(R.id.category_name)
        val accountIconImageView: ImageView = dialogView.findViewById(R.id.account_icon)
        val accountNameTextView: TextView = dialogView.findViewById(R.id.account_name)
        val transactionDescriptionTextView: TextView = dialogView.findViewById(R.id.transaction_description)
        val transactionDateTextView: TextView = dialogView.findViewById(R.id.transaction_date)

        // Transaction type
        transactionTypeTextView.text = transaction.type

        // Set background color and prefix based on transaction type
        if (transaction.type.equals("Income", ignoreCase = true)) {
            transactionAmountTextView.text = "+ RM${transaction.amount}"
            btnContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.emerald))
            amountContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.emerald))
        } else {
            transactionAmountTextView.text = "- RM${transaction.amount}"
            btnContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
            amountContainer.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
        }

        // Category Icon and Name
        val categoryIconResId = context.resources.getIdentifier(transaction.categoryIcon, "drawable", context.packageName)
        if (categoryIconResId != 0) {
            categoryIconImageView.setImageResource(categoryIconResId)
        } else {
            categoryIconImageView.setImageResource(R.drawable.ic_no_image) // Default icon if not found
        }
        categoryNameTextView.text = transaction.categoryName

        // Account Icon and Name
        val accountIconResId = context.resources.getIdentifier(transaction.accountIcon, "drawable", context.packageName)
        if (accountIconResId != 0) {
            accountIconImageView.setImageResource(accountIconResId)
        } else {
            accountIconImageView.setImageResource(R.drawable.ic_no_image) // Default icon if not found
        }
        accountNameTextView.text = transaction.accountName

        // Description and Date
        transactionDescriptionTextView.text = transaction.description
        transactionDateTextView.text = transaction.date

        // Button actions
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

    private fun deleteTransaction(context: Context, transaction: TransactionDisplay) {
        val databaseHelper = DatabaseHelper(context)
        databaseHelper.deleteTransaction(transaction.id)
        (context as? MainActivity)?.loadTransactions() // Refresh the list after deletion
    }

    fun updateData(newTransactions: List<TransactionDisplay>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
