package com.example.budgetbuddy

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        databaseHelper = DatabaseHelper(this)

        val etCategoryName: EditText = findViewById(R.id.et_category_name)
        val spinnerIcon: Spinner = findViewById(R.id.spinner_icon)
        val etBudgetLimit: EditText = findViewById(R.id.et_budget_limit)
        val tvDate: TextView = findViewById(R.id.tv_date)
        val btnSave: Button = findViewById(R.id.btn_save)
        val btnBack: Button = findViewById(R.id.btn_back)

        // Get the current date and set it to the TextView
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        tvDate.text = currentDate

        // Populate the Spinner with drawable icons and their names
        val icons = getDrawableResourceNames()
        val adapter = IconSpinnerAdapter(icons)
        spinnerIcon.adapter = adapter

        btnSave.setOnClickListener {
            val category = Category(
                id = 0,
                name = etCategoryName.text.toString(),
                icon = adapter.getItem(spinnerIcon.selectedItemPosition).name,
                budgetLimit = etBudgetLimit.text.toString().toFloat(),
                date = currentDate
            )
            databaseHelper.addCategory(category)
            finish()
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    // Implement onBackPressed method
    override fun onBackPressed() {
        super.onBackPressed()
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

//            val textView: TextView = view.findViewById(R.id.textView)
//            textView.text = iconItem.name

            return view
        }
    }

    data class IconItem(val name: String, val drawableId: Int)
}
