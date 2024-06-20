package com.example.budgetbuddy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var amount: String = ""
    private var transactionType: String = "expense"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        databaseHelper = DatabaseHelper(this)

        val btnCancel: Button = findViewById(R.id.btn_cancel)
        val btnSave: Button = findViewById(R.id.btn_save)
        val btnAccount: Button = findViewById(R.id.btn_account)
        val btnCategory: Button = findViewById(R.id.btn_category)
        val radioGroup: RadioGroup = findViewById(R.id.radio_group)

        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.white)
        }

        setupRadioGroup(radioGroup)

        btnCancel.setOnClickListener {
            // Finish the activity and return to the previous screen
            finish()
        }
    }

    private fun setupRadioGroup(radioGroup: RadioGroup) {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_income -> transactionType = "income"
                R.id.radio_expense -> transactionType = "expense"
            }
        }
    }

    companion object {
        private const val SELECT_ACCOUNT_REQUEST_CODE = 1001
    }
}


