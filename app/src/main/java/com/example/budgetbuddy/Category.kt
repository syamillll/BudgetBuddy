package com.example.budgetbuddy

data class Category(
    val id: Int,
    val name: String,
    val icon: String,
    val budgetLimit: Float,
    val date: String
)

data class Transaction(
    val id: Int,
    val categoryId: Int,
    val amount: Float,
    val date: String,
    val paymentMethod: String,
    val description: String,
    val type: String
)


