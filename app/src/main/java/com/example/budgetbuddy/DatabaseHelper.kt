package com.example.budgetbuddy

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BudgetBuddy.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_CATEGORIES = "categories"
        const val TABLE_TRANSACTIONS = "transactions"

        const val KEY_ID = "id"
        const val KEY_NAME = "name"
        const val KEY_ICON = "icon"
        const val KEY_BUDGET_LIMIT = "budget_limit"
        const val KEY_DATE = "date"

        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_AMOUNT = "amount"
        const val KEY_PAYMENT_METHOD = "payment_method"
        const val KEY_DESCRIPTION = "description"
        const val KEY_TYPE = "type"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_CATEGORIES_TABLE = ("CREATE TABLE $TABLE_CATEGORIES ("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_NAME TEXT,"
                + "$KEY_ICON TEXT,"
                + "$KEY_BUDGET_LIMIT REAL,"
                + "$KEY_DATE TEXT)")

        val CREATE_TRANSACTIONS_TABLE = ("CREATE TABLE $TABLE_TRANSACTIONS ("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_CATEGORY_ID INTEGER,"
                + "$KEY_AMOUNT REAL,"
                + "$KEY_DATE TEXT,"
                + "$KEY_PAYMENT_METHOD TEXT,"
                + "$KEY_DESCRIPTION TEXT,"
                + "$KEY_TYPE TEXT,"
                + "FOREIGN KEY($KEY_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($KEY_ID))")

        db.execSQL(CREATE_CATEGORIES_TABLE)
        db.execSQL(CREATE_TRANSACTIONS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    // Add category
    fun addCategory(category: Category): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, category.name)
            put(KEY_ICON, category.icon)
            put(KEY_BUDGET_LIMIT, category.budgetLimit)
            put(KEY_DATE, category.date)
        }
        return db.insert(TABLE_CATEGORIES, null, values)
    }

    // Get all categories
    fun getAllCategories(): List<Category> {
        val categoryList = mutableListOf<Category>()
        val selectQuery = "SELECT * FROM $TABLE_CATEGORIES"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val category = Category(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_ICON)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_BUDGET_LIMIT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE))
                )
                categoryList.add(category)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categoryList
    }

    // Add transaction
    fun addTransaction(transaction: Transaction): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_CATEGORY_ID, transaction.categoryId)
            put(KEY_AMOUNT, transaction.amount)
            put(KEY_DATE, transaction.date)
            put(KEY_PAYMENT_METHOD, transaction.paymentMethod)
            put(KEY_DESCRIPTION, transaction.description)
            put(KEY_TYPE, transaction.type)
        }
        return db.insert(TABLE_TRANSACTIONS, null, values)
    }

    // Get all transactions
    fun getAllTransactions(): List<Transaction> {
        val transactionList = mutableListOf<Transaction>()
        val selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val transaction = Transaction(
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CATEGORY_ID)),
                    cursor.getFloat(cursor.getColumnIndexOrThrow(KEY_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_PAYMENT_METHOD)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(KEY_TYPE))
                )
                transactionList.add(transaction)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return transactionList
    }

    // Update category (if needed)
    fun updateCategory(category: Category): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_NAME, category.name)
            put(KEY_ICON, category.icon)
            put(KEY_BUDGET_LIMIT, category.budgetLimit)
            put(KEY_DATE, category.date)
        }
        return db.update(TABLE_CATEGORIES, values, "$KEY_ID = ?", arrayOf(category.id.toString()))
    }

    // Delete category (if needed)
    fun deleteCategory(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CATEGORIES, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
    }

}
