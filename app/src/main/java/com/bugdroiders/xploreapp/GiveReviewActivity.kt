package com.bugdroiders.xploreapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.ActivityGiveReviewBinding
import com.bugdroiders.xploreapp.models.Booking
import com.bugdroiders.xploreapp.models.Review
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.viewModels.ReviewViewModel
import java.text.SimpleDateFormat
import java.util.Date

class GiveReviewActivity : AppCompatActivity() {
    private val view : ActivityGiveReviewBinding by lazy { ActivityGiveReviewBinding.inflate(layoutInflater) }
    private val viewModel: ReviewViewModel by viewModels<ReviewViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val cache = UserCache(this)
        val userInfo = cache.getUserInfo()

        val booking = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("booking", Booking::class.java)
        } else {
            intent.getParcelableExtra<Booking>("booking")
        }

        setSupportActionBar(view.mtbReview)
        supportActionBar?.apply {
            title="Give review about your experience"
            setDisplayHomeAsUpEnabled(true)
        }



        view.btnSubmit.setOnClickListener {
            val reviewRating = view.rbGiveReview.rating.toInt()
            val reviewText = view.etReviewText.text.toString()

            if (reviewRating != 0) {
                val review = Review(
                    rating = reviewRating,
                    reviewText = reviewText,
                    reviewDate = Date(),
                    reviewerName = booking?.travelerName,
                    reviewerProfile = userInfo?.imageUrl,
                    tourId = booking?.tourId,
                    guideId = booking?.guideId,
                )
                Log.d("CREATE REVIEW", booking.toString())
                if (booking != null) {
                    booking.id?.let { it1 -> viewModel.createReview(review, bookingId = it1) }
                }
            }
        }

        viewModel.isReviewPosted.observe(this) {
            if (it) {
                Toast.makeText(this, "Created Review Successful.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, TravelerMainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}