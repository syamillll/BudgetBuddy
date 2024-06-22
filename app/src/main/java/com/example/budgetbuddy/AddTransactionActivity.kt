package com.example.budgetbuddy

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var amount: String = ""
    private var transactionType: String = "Expense"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        databaseHelper = DatabaseHelper(this)

        val btnCancel: Button = findViewById(R.id.btn_cancel)
        val btnSave: Button = findViewById(R.id.btn_save)
        val spinnerAccount: Spinner = findViewById(R.id.spinner_account)
        val spinnerCategory: Spinner = findViewById(R.id.spinner_category)
        val radioGroup: RadioGroup = findViewById(R.id.radio_group)
        val inputAmount: EditText = findViewById(R.id.input_amount)
        val inputDescription: EditText = findViewById(R.id.input_description)
        val textDate: TextView = findViewById(R.id.text_date)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.white)
        }

        setupRadioGroup(radioGroup)
        setupAccountSpinner(spinnerAccount)
        setupCategorySpinner(spinnerCategory)

        // Set the current date
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        textDate.text = currentDate

        btnCancel.setOnClickListener {
            // Finish the activity and return to the previous screen
            finish()
        }

        btnSave.setOnClickListener {
            val amount = inputAmount.text.toString().toFloatOrNull() ?: 0f
            val description = inputDescription.text.toString()
            val categoryId = (spinnerCategory.selectedItem as Category).id
            val paymentMethod = (spinnerAccount.selectedItem as Account).name
            val date = textDate.text.toString()

            val transaction = Transaction(
                id = 0,
                categoryId = categoryId,
                amount = amount,
                date = date,
                paymentMethod = paymentMethod,
                description = description,
                type = transactionType
            )

            databaseHelper.addTransaction(transaction)
            finish() // Close the activity
        }
    }

    private fun setupRadioGroup(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_income -> transactionType = "Income"
                R.id.radio_expense -> transactionType = "Expense"
            }
        }
        radioGroup.check(R.id.radio_expense) // Set default selection to expense
    }

    private fun setupAccountSpinner(spinner: Spinner) {
        val accounts = databaseHelper.getAllAccounts()
        val adapter = CustomSpinnerAdapter(this, R.layout.spinner_item, accounts)
        spinner.adapter = adapter
    }

    private fun setupCategorySpinner(spinner: Spinner) {
        val categories = databaseHelper.getAllCategories()
        val adapter = CustomSpinnerAdapter(this, R.layout.spinner_item, categories)
        spinner.adapter = adapter
    }

    companion object {
        private const val SELECT_ACCOUNT_REQUEST_CODE = 1001
    }
}

