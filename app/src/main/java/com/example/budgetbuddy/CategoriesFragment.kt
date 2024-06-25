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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


class CategoriesFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var categoriesContainer: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val btnAddCategory: Button = view.findViewById(R.id.btn_add_category)
        categoriesContainer = view.findViewById(R.id.categories_container)

        databaseHelper = DatabaseHelper(requireContext())

        btnAddCategory.setOnClickListener {
            startActivity(Intent(requireContext(), AddCategoryActivity::class.java))
        }

        // Fetch and display categories
        displayCategories()

        return view
    }

    private fun displayCategories() {
        val categories = databaseHelper.getAllCategories()
        categoriesContainer.removeAllViews()
        for (category in categories) {
            val itemView = LayoutInflater.from(context).inflate(R.layout.item_category, categoriesContainer, false)
            val categoryIcon: ImageView = itemView.findViewById(R.id.category_icon)
            val categoryName: TextView = itemView.findViewById(R.id.category_name)

            val iconResId = resources.getIdentifier(category.icon, "drawable", requireContext().packageName)
            if (iconResId != 0) {
                categoryIcon.setImageResource(iconResId)
            } else {
                categoryIcon.setImageResource(R.drawable.ic_add)  // Use a default icon if not found
            }
            categoryName.text = category.name

            // Set click listener for item view
            itemView.setOnClickListener {
                showOptionsDialog(category)
            }

            categoriesContainer.addView(itemView)
        }
    }

    private fun showOptionsDialog(category: Category) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_category_options, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val editButton: Button = dialogView.findViewById(R.id.btn_edit)
        val deleteButton: Button = dialogView.findViewById(R.id.btn_delete)

        editButton.setOnClickListener {
            // Handle edit action
            val intent = Intent(requireContext(), EditCategoryActivity::class.java)
            intent.putExtra("category_id", category.id)  // Pass category ID for editing
            startActivity(intent)

            dialog.dismiss()
        }

        deleteButton.setOnClickListener {
            // Handle delete action
            databaseHelper.deleteCategory(category.id)
            displayCategories()  // Refresh category list after deletion
            dialog.dismiss()
        }
        editButton.setOnClickListener {
            val intent = Intent(requireContext(), EditCategoryActivity::class.java)
            intent.putExtra("category_id", category.id)
            startActivity(intent)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        displayCategories()
    }
}


