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

class EditTransactionActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var transaction: Transaction
    private var transactionType: String = "Expense"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_transaction)

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

        val transactionId = intent.getIntExtra("transaction_id", -1)
        if (transactionId != -1) {
            transaction = getTransactionById(transactionId)
            populateTransactionDetails(transaction)
        }

        setupRadioGroup(radioGroup)
        setupAccountSpinner(spinnerAccount)
        setupCategorySpinner(spinnerCategory)

        btnCancel.setOnClickListener {
            finish() // Close the activity
        }

        btnSave.setOnClickListener {
            val amount = inputAmount.text.toString().toFloatOrNull() ?: 0f
            val description = inputDescription.text.toString()
            val categoryId = (spinnerCategory.selectedItem as Category).id
            val paymentMethod = (spinnerAccount.selectedItem as Account).name
            val date = textDate.text.toString()

            val updatedTransaction = Transaction(
                id = transaction.id,
                categoryId = categoryId,
                amount = amount,
                date = date,
                paymentMethod = paymentMethod,
                description = description,
                type = transactionType
            )

            databaseHelper.updateTransaction(updatedTransaction)
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
        radioGroup.check(if (transaction.type == "Income") R.id.radio_income else R.id.radio_expense)
    }

    private fun setupAccountSpinner(spinner: Spinner) {
        val accounts = databaseHelper.getAllAccounts()
        val adapter = CustomSpinnerAdapter(this, R.layout.spinner_item, accounts)
        spinner.adapter = adapter
        val accountPosition = accounts.indexOfFirst { it.name == transaction.paymentMethod }
        if (accountPosition >= 0) {
            spinner.setSelection(accountPosition)
        }
    }

    private fun setupCategorySpinner(spinner: Spinner) {
        val categories = databaseHelper.getAllCategories()
        val adapter = CustomSpinnerAdapter(this, R.layout.spinner_item, categories)
        spinner.adapter = adapter
        val categoryPosition = categories.indexOfFirst { it.id == transaction.categoryId }
        if (categoryPosition >= 0) {
            spinner.setSelection(categoryPosition)
        }
    }

    private fun getTransactionById(id: Int): Transaction {
        val transactions = databaseHelper.getAllTransactions()
        return transactions.firstOrNull { it.id == id }
            ?: throw IllegalArgumentException("Transaction not found")
    }

    private fun populateTransactionDetails(transaction: Transaction) {
        findViewById<EditText>(R.id.input_amount).setText(transaction.amount.toString())
        findViewById<TextView>(R.id.text_date).text = transaction.date
        findViewById<EditText>(R.id.input_description).setText(transaction.description)
    }
}
