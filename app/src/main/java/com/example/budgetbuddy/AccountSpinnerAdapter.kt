// AccountSpinnerAdapter.kt
package com.example.budgetbuddy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import android.widget.ArrayAdapter

class CustomSpinnerAdapter(context: Context, @LayoutRes private val resource: Int, private val items: List<Any>) :
    ArrayAdapter<Any>(context, resource, items) {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    @NonNull
    override fun getView(position: Int, @Nullable convertView: View?, @NonNull parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(resource, parent, false)
        val itemIcon = view.findViewById<ImageView>(R.id.item_icon)
        val itemName = view.findViewById<TextView>(R.id.item_name)

        val item = items[position]
        if (item is Account) {
            itemIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
            itemName.text = item.name
        } else if (item is Category) {
            itemIcon.setImageResource(context.resources.getIdentifier(item.icon, "drawable", context.packageName))
            itemName.text = item.name
        }

        return view
    }
}
