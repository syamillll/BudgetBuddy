package com.example.budgetbuddy

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
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
        val btnCancel: Button = findViewById(R.id.btn_cancel)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.white)
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

        btnCancel.setOnClickListener {
            // Finish the activity and return to the previous screen
            finish()
        }
    }
}
