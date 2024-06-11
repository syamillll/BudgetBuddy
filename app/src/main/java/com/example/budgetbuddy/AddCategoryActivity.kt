package com.example.budgetbuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        databaseHelper = DatabaseHelper(this)

        val etCategoryName: EditText = findViewById(R.id.et_category_name)
        val etIcon: EditText = findViewById(R.id.et_icon)
        val etBudgetLimit: EditText = findViewById(R.id.et_budget_limit)
        val etDate: EditText = findViewById(R.id.et_date)
        val btnSave: Button = findViewById(R.id.btn_save)

        btnSave.setOnClickListener {
            val category = Category(
                id = 0,
                name = etCategoryName.text.toString(),
                icon = etIcon.text.toString(),
                budgetLimit = etBudgetLimit.text.toString().toFloat(),
                date = etDate.text.toString()
            )
            databaseHelper.addCategory(category)
            finish()
        }
    }
}
