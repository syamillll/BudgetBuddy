package com.example.budgetbuddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class BudgetsFragment : Fragment() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var setBudgetAdapter: SetBudgetAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budgets, container, false)
        recyclerView = view.findViewById(R.id.recyclerView_categories)

        databaseHelper = DatabaseHelper(requireContext())
        val categories = databaseHelper.getAllCategories()

        setBudgetAdapter = SetBudgetAdapter(categories.toMutableList()) { category ->
            showSetBudgetDialog(category)
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = setBudgetAdapter

        return view
    }

    private fun showSetBudgetDialog(category: Category) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_set_budget, null)
        val budgetInput = dialogView.findViewById<TextInputEditText>(R.id.budget_input)
        val monthInput = dialogView.findViewById<Spinner>(R.id.month_input)
        val yearInput = dialogView.findViewById<Spinner>(R.id.year_input)
        val setBudgetButton = dialogView.findViewById<Button>(R.id.set_budget_button)

        // Initialize months and years arrays
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        val years = (2024..2030).map { it.toString() }.toTypedArray()

        // Set initial values
        budgetInput.setText(category.budgetLimit.toString())
        val currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        // Populate spinners with data
        val monthAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            months
        )
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthInput.adapter = monthAdapter
        monthInput.setSelection(months.indexOf(months[currentMonthIndex]))

        val yearAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            years
        )
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearInput.adapter = yearAdapter
        yearInput.setSelection(years.indexOf(currentYear.toString()))

        // Set the selected values based on category.date
        val selectedYear = category.date.split("-")[0]
        val selectedMonthIndex = category.date.split("-")[1].toIntOrNull()?.minus(1) ?: currentMonthIndex

        monthInput.setSelection(selectedMonthIndex)
        yearInput.setSelection(years.indexOf(selectedYear))

        val alertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Set Budget for ${category.name}")
            .setView(dialogView)
            .create()

        setBudgetButton.setOnClickListener {
            val budget = budgetInput.text.toString().toFloatOrNull()
            val selectedMonth = months[monthInput.selectedItemPosition]
            val selectedYear = years[yearInput.selectedItemPosition]

            if (budget != null && selectedMonth.isNotEmpty() && selectedYear.isNotEmpty()) {
                val date = "$selectedYear-${monthInput.selectedItemPosition + 1}-01"
                databaseHelper.updateCategory(category.copy(budgetLimit = budget, date = date))

                // Update dataset used by RecyclerView adapter
                val updatedCategories = databaseHelper.getAllCategories()
                setBudgetAdapter.updateData(updatedCategories)

                alertDialog.dismiss()
            } else {
                // Handle invalid input scenario
                val errorMessage = when {
                    budget == null -> "Invalid budget amount"
                    selectedMonth.isEmpty() -> "Please select a month"
                    selectedYear.isEmpty() -> "Please select a year"
                    else -> "Invalid input"
                }
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        alertDialog.show()
    }
}
