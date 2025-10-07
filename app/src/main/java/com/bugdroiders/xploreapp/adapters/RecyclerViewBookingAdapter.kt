package com.bugdroiders.xploreapp.adapters

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugdroiders.xploreapp.GiveReviewActivity
import com.bugdroiders.xploreapp.databinding.BookingItemListBinding
import com.bugdroiders.xploreapp.models.Booking
import com.bugdroiders.xploreapp.models.BookingDate
import com.bugdroiders.xploreapp.models.Guide
import java.util.Date

class RecyclerViewBookingAdapter(private val context: Context, private val bookings: List<Booking>, private val isTraveler: Boolean): RecyclerView.Adapter<RecyclerViewBookingAdapter.BookingsViewHolder>() {
    class BookingsViewHolder(val binding: BookingItemListBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemClick : ((Booking) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingsViewHolder {
        val binding = BookingItemListBinding.inflate(LayoutInflater.from(context), parent, false)
        return BookingsViewHolder(binding)
    }

    override fun getItemCount(): Int = bookings.size

    override fun onBindViewHolder(holder: BookingsViewHolder, position: Int) {
        val booking = bookings[position]

        holder.binding.tvTourName.text = booking.tourName

        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val netDate = sdf.format(booking.bookingDate)
        holder.binding.tvBookingDate.text = netDate

        if (isTraveler) {
            holder.binding.tvUserType.text = "Guide Name"
            holder.binding.tvAssociate.text = booking.guideName
        } else {
            holder.binding.tvUserType.text = "Traveler Name"
            holder.binding.tvAssociate.text = booking.travelerName
        }


        if (!booking.reviewed!!) {
            holder.binding.btnReview.visibility = View.VISIBLE
        } else {
            holder.binding.btnReview.visibility = View.GONE
        }

        holder.binding.btnReview.setOnClickListener {
            onItemClick?.invoke(booking)
        }

    }


}