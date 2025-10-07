package com.bugdroiders.xploreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bugdroiders.xploreapp.databinding.TourReviewCardBinding
import com.bugdroiders.xploreapp.models.Review
import java.text.SimpleDateFormat
import java.util.Date

class RecyclerViewReviewsAdapter(private val context: Context, private val reviews: List<Review>): RecyclerView.Adapter<RecyclerViewReviewsAdapter.ReviewViewHolder>() {
    class ReviewViewHolder(val binding: TourReviewCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = TourReviewCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun getItemCount(): Int = reviews.size

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.binding.tvReviewer.text = review.reviewerName
        holder.binding.ratingBarReviewCard.rating = review.rating.toFloat()

        if (review.reviewDate != null) {
//            val milliseconds = review.reviewDate.seconds * 1000 + review.reviewDate.nanoseconds / 1000000
//            val sdf = SimpleDateFormat("dd/MM/yyyy")
//            val netDate = Date(milliseconds)
//            val date = sdf.format(netDate).toString()
            holder.binding.tvReviewDate.text = review.reviewDate.toString()
        }

        if (review.reviewerProfile != null) {
            holder.binding.imReviewer.load(
                review.reviewerProfile
            )
        }

        if (review.reviewText != null) {
            holder.binding.tvReviewDescription.text = review.reviewText
        }
    }


}