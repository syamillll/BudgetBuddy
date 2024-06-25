//package com.example.budgetbuddy
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//
//class BudgetAdapter(private val budgetLimits: BudgetLimit?) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {
//
//    inner class BudgetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val textViewAmount: TextView = itemView.findViewById(R.id.textViewAmount)
//        val textViewMonth: TextView = itemView.findViewById(R.id.textViewMonth)
//        val textViewYear: TextView = itemView.findViewById(R.id.textViewYear)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_budget, parent, false)
//        return BudgetViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
//        val budgetLimit = budgetLimits[position]
//        holder.textViewAmount.text = budgetLimit.budgetLimit.toString()
//        holder.textViewMonth.text = budgetLimit.month.toString()
//        holder.textViewYear.text = budgetLimit.year.toString()
//    }
//
//    override fun getItemCount(): Int {
//        return budgetLimits.size
//    }
//
//    fun addBudgetLimit(budgetLimit: BudgetLimit) {
//        budgetLimits.add(budgetLimit)
//        notifyItemInserted(budgetLimits.size - 1)
//    }
//}
