package com.example.budgetbuddy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class CategoriesFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val btnAddCategory: Button = view.findViewById(R.id.btn_add_category)
        val categoriesContainer: LinearLayout = view.findViewById(R.id.categories_container)

        databaseHelper = DatabaseHelper(requireContext())

        btnAddCategory.setOnClickListener {
            startActivity(Intent(requireContext(), AddCategoryActivity::class.java))
        }

        // Fetch and display categories
        displayCategories(categoriesContainer)

        return view
    }

    private fun displayCategories(container: LinearLayout) {
        val categories = databaseHelper.getAllCategories()
        container.removeAllViews()
        for (category in categories) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_category, container, false)
            val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
            val categoryName: TextView = itemView.findViewById(R.id.category_name)

            val iconResId = resources.getIdentifier(category.icon, "drawable", requireContext().packageName)
            if (iconResId != 0) {
                categoryIcon.setImageResource(iconResId)
            } else {
                categoryIcon.setImageResource(R.drawable.ic_add)  // Use a default icon if not found
            }
            categoryName.text = category.name

            container.addView(itemView)
        }
    }
}
