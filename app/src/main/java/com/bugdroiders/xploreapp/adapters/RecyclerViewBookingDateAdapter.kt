package com.bugdroiders.xploreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugdroiders.xploreapp.databinding.DateSlotItemBinding
import com.bugdroiders.xploreapp.models.BookingDate

class RecyclerViewBookingDateAdapter(private val context: Context, private val dates: List<BookingDate>): RecyclerView.Adapter<RecyclerViewBookingDateAdapter.DateViewHolder>() {
    class DateViewHolder(val binding: DateSlotItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val binding = DateSlotItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return DateViewHolder(binding)
    }

    override fun getItemCount(): Int = dates.size

    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val date = dates[position]

        holder.binding.tvDate.text = date.date.toString()
        holder.binding.tvMonth.text = date.month
    }


}