package com.bugdroiders.xploreapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bugdroiders.xploreapp.adapters.RecyclerViewReviewsAdapter
import com.bugdroiders.xploreapp.adapters.RecyclerViewTourAdapter
import com.bugdroiders.xploreapp.databinding.ActivityTravelerGuideDetailBinding
import com.bugdroiders.xploreapp.models.Guide
import com.bugdroiders.xploreapp.models.Review
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.viewModels.TravelerGuideDetailViewModel
import com.google.api.Distribution.BucketOptions.Linear

class TravelerGuideDetailActivity : AppCompatActivity() {
    private val view : ActivityTravelerGuideDetailBinding by lazy { ActivityTravelerGuideDetailBinding.inflate(layoutInflater) }
    private val viewModel : TravelerGuideDetailViewModel by viewModels<TravelerGuideDetailViewModel>()

    private val tours: MutableList<Tour> = mutableListOf()
    private val reviews: MutableList<Review> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val guide = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("guide", Guide::class.java)
        } else {
            intent.getParcelableExtra<Guide>("guide")
        }

        setSupportActionBar(view.mtbGuideDetail)
        supportActionBar?.apply {
            if (guide != null) {
                title = guide.name
            }
            setDisplayHomeAsUpEnabled(true)
        }

        if (guide != null) {
            view.tvGuideName.text = guide.name

            Log.d("GuideProfile", guide.toString())

            if (guide.image != null) {
                view.ivGuideProfile.load(
                    guide.image
                )
            }

//            viewModel.getToursOffered(guide.email.toString())
            viewModel.initializePage(guideId = guide.id.toString())
        }

        val tourAdapter = RecyclerViewTourAdapter(this, tours)
        view.rvToursOffered.adapter = tourAdapter
        view.rvToursOffered.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val reviewsAdapter = RecyclerViewReviewsAdapter(this, reviews)
        view.rvGuideReviews.adapter = reviewsAdapter
        view.rvGuideReviews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        tourAdapter.onItemClick = {
            val intent = Intent(this, GuideTourDetailActivity::class.java)
            intent.putExtra("tour", it)
            startActivity(intent)
        }

        observeView()
    }
    private fun observeView() {
        viewModel.tours.observe(this) {
            tours.clear()
            tours.addAll(it)
            view.rvToursOffered.adapter?.notifyDataSetChanged()
        }

        viewModel.reviews.observe(this) {
            reviews.clear()
            reviews.addAll(it)
            view.rvGuideReviews.adapter?.notifyDataSetChanged()
            Log.d("GuideProfile", it.toString())
        }

        viewModel.guideInfo.observe(this) {
            val reviewCount = baseContext.getString(R.string.tour_review_count, it.reviewCount)
            val spokenLanguages = it.languages?.joinToString {language -> language }
            view.tvIntroduction.text = it.introduction
            view.tvLanguages.text = spokenLanguages
            view.tvShortDescription.text = it.shortDescription
            view.guideRatingBar.rating = it.rating!!
            view.tvLocation.text = it.location
            view.tvGuideRating.text = it.rating.toString()
            view.tvReviewCount.text = reviewCount
            view.tvGuideEmail.text = it.email
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                view.pbGuideDetail.visibility = View.VISIBLE
            } else {
                view.pbGuideDetail.visibility = View.GONE
            }
        }
    }
}