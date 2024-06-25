package com.example.budgetbuddy

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class EditCategoryActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var etCategoryName: EditText
    private lateinit var spinnerIcon: Spinner
    private lateinit var etBudgetLimit: EditText
    private lateinit var tvDate: TextView
    private var categoryId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_category)

        databaseHelper = DatabaseHelper(this)

        etCategoryName = findViewById(R.id.et_category_name)
        spinnerIcon = findViewById(R.id.spinner_icon)
        etBudgetLimit = findViewById(R.id.et_budget_limit)
        tvDate = findViewById(R.id.tv_date)
        val btnSave: Button = findViewById(R.id.btn_save)
        val btnBack: Button = findViewById(R.id.btn_back)

        categoryId = intent.getIntExtra("category_id", 0)
        val category = databaseHelper.getCategory(categoryId)

        // Populate fields with category data
        etCategoryName.setText(category.name)
        etBudgetLimit.setText(category.budgetLimit.toString())
        tvDate.text = category.date

        // Populate the Spinner with drawable icons and their names
        val icons = getDrawableResourceNames()
        val adapter = IconSpinnerAdapter(icons)
        spinnerIcon.adapter = adapter

        // Set the spinner to the current icon
        val iconIndex = icons.indexOfFirst { it.name == category.icon }
        if (iconIndex != -1) {
            spinnerIcon.setSelection(iconIndex)
        }

        btnSave.setOnClickListener {
            val newName = etCategoryName.text.toString()
            val newIcon = adapter.getItem(spinnerIcon.selectedItemPosition).name
            val newBudgetLimit = etBudgetLimit.text.toString().toFloatOrNull() ?: 0.0f
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val updatedCategory = category.copy(
                name = newName,
                icon = newIcon,
                budgetLimit = newBudgetLimit,
                date = currentDate
            )

            val result = databaseHelper.updateCategory(updatedCategory)
            if (result > 0) {
                setResult(Activity.RESULT_OK)
                finish() // Finish activity after update
            } else {
                // Handle update failure, e.g., show a toast
                Toast.makeText(this, "Failed to update category", Toast.LENGTH_SHORT).show()
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getDrawableResourceNames(): List<IconItem> {
        val resources = resources
        val icons = mutableListOf<IconItem>()

        val drawableNames = resources.getStringArray(R.array.drawable_names)
        for (drawableName in drawableNames) {
            val drawableId = resources.getIdentifier(drawableName, "drawable", packageName)
            if (drawableId != 0) {
                icons.add(IconItem(drawableName, drawableId))
            }
        }

        return icons
    }

    class IconSpinnerAdapter(private val icons: List<IconItem>) : BaseAdapter() {
        override fun getCount(): Int {
            return icons.size
        }

        override fun getItem(position: Int): IconItem {
            return icons[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val iconItem = icons[position]

            val imageView = ImageView(parent?.context)
            imageView.setImageResource(iconItem.drawableId)
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            imageView.adjustViewBounds = true
            imageView.layoutParams = ViewGroup.LayoutParams(100, 100)

            return imageView
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val iconItem = icons[position]

            val view = View.inflate(parent?.context, R.layout.spinner_item_icon, null)
            val imageView: ImageView = view.findViewById(R.id.imageView)
            imageView.setImageResource(iconItem.drawableId)

            return view
        }
    }

    data class IconItem(val name: String, val drawableId: Int)
}
