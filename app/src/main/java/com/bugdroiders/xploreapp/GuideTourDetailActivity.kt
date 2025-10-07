package com.bugdroiders.xploreapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bugdroiders.xploreapp.adapters.RecyclerViewReviewsAdapter
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.databinding.ActivityGuideTourDetailBinding
import com.bugdroiders.xploreapp.models.Review
import com.bugdroiders.xploreapp.models.Schedule
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.viewModels.GuideTourDetailViewModel
import com.google.android.material.tabs.TabLayout

class GuideTourDetailActivity : AppCompatActivity() {
    private val view : ActivityGuideTourDetailBinding by lazy { ActivityGuideTourDetailBinding.inflate(layoutInflater) }

    private val viewModel : GuideTourDetailViewModel by viewModels<GuideTourDetailViewModel> ()
    private val reviews: MutableList<Review> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        setSupportActionBar(view.toolbarGuideTourDetail)

        val tour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("tour", Tour::class.java)
        } else {
            intent.getParcelableExtra<Tour>("tour")
        }

        supportActionBar?.apply {
            title = tour?.title ?: "Edit my tour"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }

        if (tour != null) {
            view.tvTourTitle.text = tour.title
            if (tour.image?.isNotEmpty() == true) {
                view.ivTourImage.load(
                    tour.image
                )
            }
            viewModel.getTourDetail(tour.id.toString())
            viewModel.getTourReviews(tour.id.toString())
        }

        val reviewAdapter = RecyclerViewReviewsAdapter(this, reviews)
        view.rcReviews.adapter = reviewAdapter
        view.rcReviews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        view.tabLayoutTourDetail.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {
                            view.scrollViewTourDetail.scrollTo(0, view.ivTourImage.top)
                        }
                        1 -> {
                            view.scrollViewTourDetail.scrollTo(0, view.tvItineryText.top)
                        }
                        2 -> {
                            view.scrollViewTourDetail.scrollTo(0, view.tvReviewsHeader.top)
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position) {
                        0 -> {
                            view.scrollViewTourDetail.scrollTo(0, view.ivTourImage.top)
                        }
                        1 -> {
                            view.scrollViewTourDetail.scrollTo(0, view.tvItineryText.top)
                        }
                        2 -> {
                            view.scrollViewTourDetail.scrollTo(0, view.tvReviewsHeader.top)
                        }
                    }
                }
            }

        })

        view.tvSeeReviews.setOnClickListener {
            view.scrollViewTourDetail.scrollTo(0, view.tvReviewsHeader.top)
        }

        observeView()
    }

    private fun observeView() {
        viewModel.isDeleted.observe(this) {
            if (it) {
                Toast.makeText(this@GuideTourDetailActivity, "Deleted Tour Successfully.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@GuideTourDetailActivity, GuideMainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@GuideTourDetailActivity, "Delete Tour Failed.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                view.progressBarGuideTourDetail.visibility = View.VISIBLE
            } else {
                view.progressBarGuideTourDetail.visibility = View.GONE
            }
        }

        viewModel.reviews.observe(this) {
            reviews.clear()
            reviews.addAll(it)
            view.rcReviews.adapter?.notifyDataSetChanged()
        }

        viewModel.tour.observe(this) {it ->
            if (it != null) {

                val reviewCount = baseContext.getString(R.string.tour_review_count, it.reviewCount)

                view.tvReviewCount.text = reviewCount
                view.tvRatingScore.text = it.rating.toString()
                view.tvTourGuideName.text = it.guide.toString()
                view.tvTourDescription.text = it.description.toString()
                view.tvTourLocation.text = it.tourLocation.toString()
                view.tvTourType.text = it.tourType.toString()

                view.tvOverallRating.text = it.rating.toString()
                view.tvTotalReviews.text = reviewCount

                view.ratingBarTourDetail.rating = it.rating?.toFloat() ?: 0f

                if (it.guideImage?.isNotEmpty() == true) {
                    view.ivTourGuideProfile.load(
                        it.guideImage
                    )
                }

                if (it.schedule != null) {
                    for (item in it.schedule) {
                        addScheduleListItem(view.llScheduleList, item)
                    }
                }
            }
        }
    }

    private fun addScheduleListItem(parentLayout: LinearLayout, scheduleItem: Schedule) {
        val inflater = LayoutInflater.from(this)
        val itemView = inflater.inflate(R.layout.schedule_list_item, parentLayout, false)

        val scheduleTitle: TextView = itemView.findViewById(R.id.tvScheduleTitle)
        val scheduleDescription: TextView = itemView.findViewById(R.id.tvScheduleDescription)

        scheduleTitle.text = scheduleItem.title
        scheduleDescription.text = scheduleItem.description

        parentLayout.addView(itemView)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.guide_tour_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Dialog box to show when delete tour
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this tour?")
            .setTitle("Confirm to delete")
            .setPositiveButton("Delete") {dialog, which ->
                // Do something
                Log.d("Tour","Delete button clicked")
                if (viewModel.tour.value?.id != null) {
                    viewModel.deleteTour()
                }
            }
            .setNegativeButton("Cancel") {dialog, which ->
                Log.d("Tour", "Cancel button clicked")
            }

        return when (item.itemId) {
            R.id.deleteTourMenu -> {
                Log.d("Tour", "Delete Tour menu button clicked")
                val dialog: AlertDialog = builder.create()
                dialog.show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}