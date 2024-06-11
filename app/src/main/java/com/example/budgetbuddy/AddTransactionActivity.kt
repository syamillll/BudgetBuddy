package com.example.budgetbuddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        databaseHelper = DatabaseHelper(this)

        val etAmount: EditText = findViewById(R.id.et_amount)
        val etDate: EditText = findViewById(R.id.et_date)
        val etPaymentMethod: EditText = findViewById(R.id.et_payment_method)
        val etDescription: EditText = findViewById(R.id.et_description)
        val etCategoryId: EditText = findViewById(R.id.et_category_id)
        val etType: EditText = findViewById(R.id.et_type)
        val btnSave: Button = findViewById(R.id.btn_save)

        btnSave.setOnClickListener {
            val transaction = Transaction(
                id = 0,
                categoryId = etCategoryId.text.toString().toInt(),
                amount = etAmount.text.toString().toFloat(),
                date = etDate.text.toString(),
                paymentMethod = etPaymentMethod.text.toString(),
                description = etDescription.text.toString(),
                type = etType.text.toString()
            )
            databaseHelper.addTransaction(transaction)
            finish()
        }
        btnSave.setOnClickListener {
            val transaction = Transaction(
                id = 0,
                categoryId = etCategoryId.text.toString().toInt(),
                amount = etAmount.text.toString().toFloat(),
                date = etDate.text.toString(),
                paymentMethod = etPaymentMethod.text.toString(),
                description = etDescription.text.toString(),
                type = etType.text.toString()
            )

            if (transaction.type == "expense" && databaseHelper.checkBudgetExceeded(transaction.categoryId, transaction.amount)) {
                // Show warning to the user
                Toast.makeText(this, "Budget exceeded for this category!", Toast.LENGTH_LONG).show()
            } else {
                databaseHelper.addTransaction(transaction)
                finish()
            }
        }

    }
}
