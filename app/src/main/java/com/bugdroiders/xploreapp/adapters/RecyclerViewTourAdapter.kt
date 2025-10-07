package com.bugdroiders.xploreapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bugdroiders.xploreapp.R
import com.bugdroiders.xploreapp.TourDetailActivity
import com.bugdroiders.xploreapp.databinding.TourListItemBinding
import com.bugdroiders.xploreapp.models.Tour

class RecyclerViewTourAdapter(private val context: Context, private val tours: List<Tour>): RecyclerView.Adapter<RecyclerViewTourAdapter.TourViewHolder>() {
    class TourViewHolder(val binding: TourListItemBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemClick : ((Tour) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourViewHolder {
        val binding = TourListItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return TourViewHolder(binding)
    }

    override fun getItemCount(): Int = tours.size

    override fun onBindViewHolder(holder: TourViewHolder, position: Int) {
        val tour = tours[position]
        holder.binding.ivTour.load(
            tour.image
        )
        holder.binding.tvTitle.text = tour.title
        holder.binding.tvRating.text = tour.rating.toString()
        val reviewCount = context.getString(R.string.tour_review_count, tour.reviewCount)
        holder.binding.tvReviewCount.text = reviewCount
        val priceText = context.getString(R.string.tour_price, tour.price)
        holder.binding.tvPrice.text = priceText
        val tourGuideText = context.getString(R.string.tour_guide_name, tour.guide)
        holder.binding.tvTourGuide.text = tourGuideText
//        holder.binding.root.setOnClickListener {
//            val intent = Intent(holder, TourDetailActivity::class.java)
//
//        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tour)
        }
    }
}