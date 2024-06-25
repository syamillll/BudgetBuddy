package com.example.budgetbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class Account(
    val id: Int,
    val name: String,
    val icon: String,
    val balance: Float
)

class AccountsAdapter(
    private var accounts: List<Account>,
    private val onAccountClick: (Account) -> Unit
) : RecyclerView.Adapter<AccountsAdapter.AccountViewHolder>() {

    inner class AccountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val accountName: TextView = itemView.findViewById(R.id.accountName)
        private val accountBalance: TextView = itemView.findViewById(R.id.accountBalance)
        private val accountIcon: ImageView = itemView.findViewById(R.id.accountIcon)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val account = accounts[position]
                    onAccountClick(account)
                }
            }
        }

        fun bind(account: Account) {
            accountName.text = account.name
            accountBalance.text = account.balance.toString()
            val context = itemView.context
            val iconResId = context.resources.getIdentifier(account.icon, "drawable", context.packageName)
            if (iconResId != 0) {
                accountIcon.setImageResource(iconResId)
            } else {
                accountIcon.setImageResource(R.drawable.ic_accounts) // Default icon if not found
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(accounts[position])
    }

    override fun getItemCount() = accounts.size
}
