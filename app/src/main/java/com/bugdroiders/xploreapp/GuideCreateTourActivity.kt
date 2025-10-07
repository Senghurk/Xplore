package com.bugdroiders.xploreapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import coil.load
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.ActivityGuideCreateTourBinding
import com.bugdroiders.xploreapp.models.Schedule
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.viewModels.GuideTourViewModel

class GuideCreateTourActivity : AppCompatActivity() {
    private val view : ActivityGuideCreateTourBinding by lazy { ActivityGuideCreateTourBinding.inflate(layoutInflater) }
    private val viewModel: GuideTourViewModel by viewModels<GuideTourViewModel>()

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        Log.d("Tour", uri.toString())
        view.ivAddTour.load(
            uri
        )
        viewModel.tourImageUri.postValue(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        val userCache = UserCache(this)
        val userInfo = userCache.getUserInfo()
        var scheduleCount = 0
        val tourSchedule = mutableListOf<Schedule>()

        setSupportActionBar(view.toolbar)

        supportActionBar?.apply {
            title="Create New Tour"
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
        }

        view.ivAddTour.setOnClickListener {
            Log.d("Upload image", "User clicked on the image view.")
            pickImage()
        }


        val parentLayout : LinearLayout = findViewById(R.id.tourScheduleLayout)

        view.addTourSchedule.setOnClickListener {
            val scheduleTitle = view.etTourScheduleTitle.text
            val scheduleDescription = view.etTourScheduleDescription.text
            if (scheduleTitle.isNullOrEmpty() && scheduleDescription.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show()
            } else {
                scheduleCount += 1
                val schedule = Schedule(
                    title = scheduleTitle.toString(),
                    description = scheduleDescription.toString()
                )
                tourSchedule.add(schedule)
                Log.d("TourSchedule", tourSchedule.toString())
                addNewScheduleSection(parentLayout, scheduleCount, scheduleTitle.toString(), scheduleDescription.toString())
                scheduleTitle?.clear()
                scheduleDescription?.clear()
            }
        }

        view.btnCreateTour.setOnClickListener {
            Log.d("Create New Tour", "User clicked on create new tour")
            val tourTitle = view.etTourTitle.text.toString()
            val tourDescription = view.etTourDescription.text.toString()
            val tourPriceText = view.etTourPrice.text.toString()
            val tourLocation = view.etTourLocation.text.toString()
            val tourType = view.etTourType.text.toString()
            val tourDuration = view.etTourDuration.text.toString()

            if (tourTitle.isNotEmpty() && tourDescription.isNotEmpty()) {
                if (tourPriceText.isEmpty()) {
                    Toast.makeText(this@GuideCreateTourActivity, "Please fill in the Price", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        val tourPrice = tourPriceText.toInt()
                        if (tourPrice > 0) {
                            val tour = Tour(
                                title = tourTitle,
                                description = tourDescription,
                                tourDuration = tourDuration,
                                tourLocation = tourLocation,
                                price = tourPrice,
                                guide = userInfo?.fullName,
                                guideId = userInfo?.email,
                                guideImage = userInfo?.imageUrl,
                                tourType = tourType,
                                schedule = tourSchedule
                            )

                            viewModel.createTour(tour)
                        } else {
                            Toast.makeText(this@GuideCreateTourActivity, "Please fill in the information", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e : NumberFormatException) {
                        Toast.makeText(this@GuideCreateTourActivity, "Please fill in number for the price", Toast.LENGTH_SHORT).show()
                    }
                }
            }  else {
                Toast.makeText(this@GuideCreateTourActivity, "Please fill in the information", Toast.LENGTH_SHORT).show()
            }
        }

        observeView()
    }

    private fun observeView() {
        viewModel.isTourCreated.observe(this) {
            if (it) {
                Toast.makeText(this@GuideCreateTourActivity, "Created Tour Successfully.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@GuideCreateTourActivity, GuideMainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@GuideCreateTourActivity, "Created Tour Failed.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(this) {
            if (it) {
                view.createTourProgressBar.visibility = View.VISIBLE
                view.btnCreateTour.isEnabled = false
            } else {
                view.createTourProgressBar.visibility = View.GONE
            }
        }
    }

    private fun pickImage() {
        pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun addNewScheduleSection(parentLayout: LinearLayout, index: Int, title:String, description: String) {
        val newScheduleCount = TextView(this)
        val scheduleText = "Schedule $index"
        newScheduleCount.text = scheduleText
        val newScheduleTitle = TextView(this)
        newScheduleTitle.text = title
        val newScheduleDescription = TextView(this)
        newScheduleDescription.text = description

        newScheduleCount.textSize = 20f
        newScheduleCount.setTypeface(null, Typeface.BOLD)
        newScheduleCount.setTextColor(Color.GRAY)
        newScheduleTitle.textSize = 20f
        newScheduleTitle.setTypeface(null, Typeface.BOLD)
        newScheduleDescription.textSize = 16f
        val newTextField = EditText(this)
        newTextField.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        parentLayout.addView(newScheduleCount)
        parentLayout.addView(newScheduleTitle)
        parentLayout.addView(newScheduleDescription)
    }
}