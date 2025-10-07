package com.bugdroiders.xploreapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bugdroiders.xploreapp.R
import com.bugdroiders.xploreapp.databinding.GuideListItemBinding
import com.bugdroiders.xploreapp.models.Guide

class RecyclerViewGuidesListAdapter(private val context: Context, private val guides: List<Guide>): RecyclerView.Adapter<RecyclerViewGuidesListAdapter.GuideViewHolder>() {
    class GuideViewHolder(val binding: GuideListItemBinding) : RecyclerView.ViewHolder(binding.root)

    var onItemClick : ((Guide) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val binding = GuideListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return GuideViewHolder(binding)
    }

    override fun getItemCount(): Int = guides.size

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guides[position]

        if (guide.image != null) {
            holder.binding.ivGuideProfile.load(
                guide.image
            )
        } else {
            holder.binding.ivGuideProfile.load(
                R.drawable.baseline_account_circle_24
            )
        }

        val spokenLanguages = guide.languages?.joinToString { it ->
            it
        }
        val guideNameText = context.getString(R.string.guide_name, guide.name, guide.introduction)
        val reviewCount = context.getString(R.string.guide_review_count, guide.reviewCount)
        val guideAddress = context.getString(R.string.guide_address, guide.location)
        val guideLanguages = context.getString(R.string.guide_languages, spokenLanguages)
        holder.binding.tvGuideName.text = guideNameText
        holder.binding.tvGuideReviewCount.text = reviewCount
        holder.binding.tvArea.text = guideAddress
        holder.binding.tvGuideRating.text = guide.rating.toString()
        holder.binding.tvLanguages.text = guideLanguages

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(guide)
        }
    }
}