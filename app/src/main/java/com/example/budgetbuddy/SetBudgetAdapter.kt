package com.example.budgetbuddy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SetBudgetAdapter(
    private var categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<SetBudgetAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryName: TextView = view.findViewById(R.id.category_name_textview_set_budget)
        val categoryDateTextView: TextView = view.findViewById(R.id.category_date_textview_set_budget)
        val categoryIcon: ImageView = view.findViewById(R.id.category_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category_set_budget, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryName.text = category.name
        holder.categoryDateTextView.text = formatDate(category.date)
        holder.categoryIcon.setImageResource(
            holder.itemView.context.resources.getIdentifier(
                category.icon,
                "drawable",
                holder.itemView.context.packageName
            )
        )

        holder.itemView.setOnClickListener {
            onCategoryClick(category)
        }
    }

    override fun getItemCount() = categories.size

    fun updateData(newCategories: List<Category>) {
        categories = newCategories  // Update categories list
        notifyDataSetChanged()  // Notify adapter of dataset change
    }

    private fun formatDate(dateString: String): String {
        val parts = dateString.split("-")
        if (parts.size != 3) return ""

        val year = parts[0].toIntOrNull() ?: return ""
        val monthIndex = parts[1].toIntOrNull()?.minus(1) ?: return ""

        val calendar = Calendar.getInstance()
        calendar.set(year, monthIndex, 1)

        val monthName = SimpleDateFormat("MMMM", Locale.getDefault()).format(calendar.time)
        return "$monthName $year"
    }
}


