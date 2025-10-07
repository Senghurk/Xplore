package com.bugdroiders.xploreapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.bugdroiders.xploreapp.adapters.RecyclerViewBookingDateAdapter
import com.bugdroiders.xploreapp.adapters.RecyclerViewReviewsAdapter
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.ActivityTourDetailBinding
import com.bugdroiders.xploreapp.models.Booking
import com.bugdroiders.xploreapp.models.BookingDate
import com.bugdroiders.xploreapp.models.Guide
import com.bugdroiders.xploreapp.models.Schedule
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.models.UserType
import com.bugdroiders.xploreapp.viewModels.TourDetailViewModel
import com.google.android.material.tabs.TabLayout

class TourDetailActivity : AppCompatActivity() {
    private val view : ActivityTourDetailBinding by lazy { ActivityTourDetailBinding.inflate(layoutInflater) }
    private val viewModel: TourDetailViewModel by viewModels<TourDetailViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val cache = UserCache(this)
        val userInfo = cache.getUserInfo()

        val tour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("tour", Tour::class.java)
        } else {
            intent.getParcelableExtra<Tour>("tour")
        }

        setSupportActionBar(view.materialToolbarTour)

        supportActionBar?.apply {
            if (tour != null) {
                title=tour.title
            }
            setDisplayHomeAsUpEnabled(true)
        }

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

        view.llGuideInfo.setOnClickListener {
            val selectedGuide = Guide(
                id = tour?.guideId,
                name = tour?.guide,
                image = tour?.guideImage
            )
            val intent = Intent(this, TravelerGuideDetailActivity::class.java)
            intent.putExtra("guide", selectedGuide)
            startActivity(intent)
        }

        view.tvSeeReviews.setOnClickListener {
            view.scrollViewTourDetail.scrollTo(0, view.tvReviewsHeader.top)
        }

        view.btnBookTour.setOnClickListener {
            if (userInfo != null) {
                viewModel.bookTour(userInfo)
            }
        }

//        val bookingDates = listOf<BookingDate>(
//            BookingDate(12, "Feb"),
//            BookingDate(13, "Feb"),
//            BookingDate(14, "Feb"),
//            BookingDate(15, "Feb"),
//            BookingDate(16, "Feb"),
//            BookingDate(17, "Feb")
//        )
//        val reviewAdapter = RecyclerViewBookingDateAdapter(this, bookingDates)
//        view.rvBookingslot.adapter = reviewAdapter
//        view.rvBookingslot.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        if (tour != null) {
            Log.d("TourDetailActivity", tour.id.toString())
            view.tvTourTitle.text = tour.title
            if (tour.image?.isNotEmpty() == true) {
                view.ivTourImage.load(
                    tour.image
                )
            }

            viewModel.getTourDetail(tour.id.toString())
            viewModel.getTourReviews(tour.id.toString())
        }

        observeView()
    }

    private fun observeView() {
        viewModel.tourInfo.observe(this) {
            if (it != null) {

                val reviewCount = baseContext.getString(R.string.tour_review_count, it.reviewCount)
                val tourPrice = baseContext.getString(R.string.tour_price, it.price)

                view.tvReviewCount.text = reviewCount
                view.tvTourPrice.text = tourPrice
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

        viewModel.reviews.observe(this) {
            Log.d("Reviews", it.toString())
            if (it != null) {
                val reviewAdapter = RecyclerViewReviewsAdapter(this, it)
                view.rcReviews.adapter = reviewAdapter
                view.rcReviews.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        viewModel.isBooked.observe(this) {
            Toast.makeText(this, "Tour booked successfully", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TravelerMainActivity::class.java)
            startActivity(intent)
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                view.pbTourDetail.visibility = View.VISIBLE
            } else {
                view.pbTourDetail.visibility = View.GONE
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
}
