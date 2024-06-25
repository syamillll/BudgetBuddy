package com.example.budgetbuddy

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class AnalysisFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var spinnerTransactionType: Spinner
    private lateinit var db: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_analysis, container, false)

        pieChart = view.findViewById(R.id.pie_chart)
        spinnerTransactionType = view.findViewById(R.id.spinner_transaction_type)
        db = DatabaseHelper(requireContext())

        setupSpinner()

        return view
    }

    private fun setupSpinner() {
        val transactionTypes = listOf("All", "Expense", "Income")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, transactionTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTransactionType.adapter = adapter

        spinnerTransactionType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedType = transactionTypes[position]
                updatePieChart(selectedType)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun updatePieChart(transactionType: String) {
        val transactions = when (transactionType) {
            "Expense" -> db.getTransactionsByType("Expense")
            "Income" -> db.getTransactionsByType("Income")
            else -> db.getAllTransactions()
        }

        val categoryTotals = transactions.groupBy { it.categoryId }
            .mapValues { entry -> entry.value.sumOf { it.amount.toDouble() } }

        val pieEntries = categoryTotals.map { (categoryId, total) ->
            val category = db.getCategoryById(categoryId)
            PieEntry(total.toFloat(), category.name)
        }

        val dataSet = PieDataSet(pieEntries, "Transactions by Category")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.invalidate() // refresh the chart
    }
}
