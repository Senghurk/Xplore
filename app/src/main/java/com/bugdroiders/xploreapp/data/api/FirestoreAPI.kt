package com.bugdroiders.xploreapp.data.api

import android.util.Log
import com.bugdroiders.xploreapp.models.Booking
import com.bugdroiders.xploreapp.models.Guide
import com.bugdroiders.xploreapp.models.Review
import com.bugdroiders.xploreapp.models.Tour
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class FirestoreAPI {
    private val db = Firebase.firestore
    suspend fun getToursSnapshot() : List<Tour> {
        val tours = mutableListOf<Tour>()

        try {
            val snapshot = db.collection("tours")
                .get()
                .await()

            for (document in snapshot.documents) {
                val tour = document.toObject<Tour>()
                tour?.let {
                    tours.add(it)
                }
            }
        } catch (e: Exception) {
            Log.e("GetToursSnapshot", "Error getting tours: ${e.message}")
        }
        return tours
    }

    suspend fun getTours(guideId : String) : List<Tour> {
        val tours = mutableListOf<Tour>()

        try {
            val snapshot = db.collection("tours")
                .whereEqualTo("guideId", guideId)
                .get().await()

            for (document in snapshot.documents) {
                val tour = document.toObject<Tour>()
                tour?.let {
                    tours.add(it)
                }
            }
        } catch (e: Exception) {
            Log.e("GetTours", "Error getting tours: ${e.message}")
        }

        return tours
    }

    suspend fun getTour(tourId: String) : Tour? {
        var tour: Tour? = null
        try {
            val tourDocumentSnapshot = db.collection("tours")
                .document(tourId)
                .get()
                .await()

            if (tourDocumentSnapshot.exists()) {
                tour = tourDocumentSnapshot.toObject<Tour>()
            } else {
                Log.d("Tour", "No such document")
            }
        } catch (e: Exception) {
            Log.e("Tour", "Error getting tour: ${e.message}")
        }

        return tour
    }
//    }

    suspend fun getTourReviews(tourId: String): List<Review> {
        val reviews = mutableListOf<Review>()
        try {
            val snapshot = db.collection("tours")
                .document(tourId)
                .collection("reviews")
                .get()
                .await()

            for (document in snapshot.documents) {
                val review = document.toObject<Review>()
                review?.let { reviews.add(it) }
            }
        } catch (e: Exception) {
            Log.e("GetGuideReview", "Error getting guide reviews: ${e.message}")
        }

        return reviews
    }

    suspend fun getGuideReview(guideId: String): List<Review> {
        val reviews = mutableListOf<Review>()
        try {
            val snapshot = db.collection("guides")
                .document(guideId)
                .collection("reviews")
                .get()
                .await()

            for (document in snapshot.documents) {
                val review = document.toObject<Review>()
                review?.let {
                    reviews.add(it)
                }
            }
        } catch (e: Exception) {
            Log.e("GetGuideReview", "Error getting guide reviews: ${e.message}")
        }
//        db.collection("guides")
//            .document(guideId)
//            .collection("reviews")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    Log.d("TourDetail", "${document.id} => ${document.data}")
//                    reviews.add(document.toObject<Review>())
//                }
//            }
//            .addOnFailureListener {exception ->
//                Log.d("TourDetail", "Error getting documents: ", exception)
//            }
//            .await()
        return reviews
    }

    suspend fun getGuidesSnapshot() : List<Guide> {
        val guides = mutableListOf<Guide>()
        try {
            val snapshot = db.collection("guides")
                .get()
                .await()

            for (document in snapshot.documents) {
                val guide = document.toObject<Guide>()
                guide?.let {
                    guides.add(guide)
                }
            }
        } catch (e:Exception) {
            Log.e("GetGuidesSnapshot", "Error getting guides: ${e.message}")
        }

        return guides
    }

    suspend fun getGuide(guideEmail: String) : DocumentSnapshot? {
        try {
            val guideSnapshot = db.collection("guides").document(guideEmail).get().await()
            return guideSnapshot
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun isGuideExists(guideEmail: String) : Boolean {
        var isGuideFound = false
        val guideRef = db.collection("guides")

        try {
            val resultDoc = guideRef.whereEqualTo("email", guideEmail).get().await()

            if (!resultDoc.isEmpty) {
                Log.d("ProfileExists", "This is called.")
                isGuideFound = true
            } else {
                Log.d("ProfileExists", "Guide Not found.")
                isGuideFound = false
            }
        } catch (e: Exception) {
            isGuideFound = false
        }
        Log.d("ProfileExists", "What about this?")
        return isGuideFound
    }

    suspend fun createReview(review: Review, bookingId: String): Boolean {
        var isCreated = false
        try {
            review.tourId?.let { tourId ->
                val tourRef = db.collection("tours").document(tourId)
                val tourSnapshot = tourRef.get().await()
                if (tourSnapshot.exists()) {
                    db.collection("tours").document(tourId).collection("reviews")
                        .add(review)
                        .await()

                    val ratingIncrement = review.rating.toLong()
                    val newTourRating = tourSnapshot.getDouble("totalRating")?.plus(ratingIncrement)
                        ?: ratingIncrement
                    val newReviewCount = tourSnapshot.getLong("reviewCount")?.plus(1) ?: 1

                    val avgRating = newTourRating.toFloat() / newReviewCount.toFloat()

                    db.collection("tours")
                        .document(tourId)
                        .update(
                            "totalRating",
                            newTourRating,
                            "reviewCount",
                            newReviewCount,
                            "rating",
                            avgRating
                        )
                        .await()

                    isCreated = true
                }

                review.guideId?.let { guideId ->
                    val guideRef = db.collection("guides").document(guideId)
                    val guideSnapshot = guideRef.get().await()
                    if (guideSnapshot.exists()) {
                        db.collection("guides").document(guideId).collection("reviews")
                            .add(review)
                            .await()

                        val ratingIncrement = review.rating.toLong()
                        val newGuideRating =
                            guideSnapshot.getDouble("totalRating")?.plus(ratingIncrement)
                                ?: ratingIncrement
                        val newReviewCount = guideSnapshot.getLong("reviewCount")?.plus(1) ?: 1

                        val avgRating = newGuideRating.toFloat() / newReviewCount.toFloat()

                        db.collection("guides")
                            .document(guideId)
                            .update(
                                "totalRating",
                                newGuideRating,
                                "reviewCount",
                                newReviewCount,
                                "rating",
                                avgRating
                            )
                            .await()

                        isCreated = true
                    }
                }

                db.collection("bookings")
                    .document(bookingId)
                    .update("reviewed", true)
                    .await()
            }
        } catch (e: Exception) {
            Log.e("CreateReview", "Error creating review: ${e.message}")
        }

        return isCreated
    }
//        review.tourId?.let {
//            db.collection("tours").document(it).collection("reviews")
//                .add(review)
//                .addOnSuccessListener {
//                    isCreated = true
//                }
//                .addOnFailureListener {
//
//                }
//                .await()
//
//            db.collection("tours").document(it)
//                .update("totalRating", FieldValue.increment(review.rating.toLong())).await()
//            val tourData = getTour(it)?.toObject<Tour>()
//            val newReviewCount = tourData?.reviewCount?.plus(1)
//
//            val avgRating = tourData?.totalRating?.toFloat()?.div(newReviewCount!!)
//
//            db.collection("tours")
//                .document(it)
//                .update("rating", avgRating, "reviewCount", newReviewCount)
//                .await()
//
//        }
//
//        db.collection("bookings")
//            .document(bookingId)
//            .update("reviewed", true)
//            .await()
//
//
//
//        review.guideId?.let {
//            db.collection("guides").document(it).collection("reviews")
//                .add(review)
//                .addOnSuccessListener {
//                    isCreated = true
//                }
//                .addOnFailureListener {
//
//                }
//                .await()
//
//            db.collection("guides").document(it)
//                .update("totalRating", FieldValue.increment(review.rating.toLong())).await()
//            val guideData = getGuide(it)?.toObject<Guide>()
//            val newReviewCount = guideData?.reviewCount?.plus(1)
//
//            val avgRating = guideData?.totalRating?.toFloat()?.div(newReviewCount!!)
//
//            db.collection("guides")
//                .document(it)
//                .update("rating", avgRating, "reviewCount", newReviewCount)
//                .await()
//        }
//        return isCreated


    suspend fun bookTour(booking: Booking) : Boolean {
        var isCreated = false
        try {
            val document = db.collection("bookings").add(booking).await()
            isCreated = true
        } catch (e: Exception) {
            isCreated = false
        }

        return isCreated
    }

    suspend fun getBookingsTraveler(travelerEmail: String) : List<Booking> {
        val bookings = mutableListOf<Booking>()

        try {
            val snapshot = db.collection("bookings")
                .whereEqualTo("travelerId", travelerEmail)
                .get()
                .await()

            for (document in snapshot.documents) {
                val booking = document.toObject<Booking>()
                booking?.let {
                    bookings.add(it)
                }
            }
        } catch (e: Exception) {
            Log.e("GetBookingsTraveler", "Error getting bookings: ${e.message}")
        }
//        db.collection("bookings")
//            .whereEqualTo("travelerId", travelerEmail)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    bookings.add(document.toObject<Booking>())
//                }
//            }
//            .addOnFailureListener {
//
//            }
//            .await()
        return bookings
    }

    suspend fun getGuideBookings(guideEmail: String) : List<Booking> {
        val bookings = mutableListOf<Booking>()

        try {
            val snapshot = db.collection("bookings")
                .whereEqualTo("guideId", guideEmail)
                .get()
                .await()

            for (document in snapshot.documents) {
                val booking = document.toObject<Booking>()
                booking?.let {
                    bookings.add(it)
                }
            }
        }  catch (e: Exception) {
            Log.e("GetBookingsTraveler", "Error getting bookings: ${e.message}")
        }
//        db.collection("bookings")
//            .whereEqualTo("guideId", guideEmail)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    bookings.add(document.toObject<Booking>())
//                }
//            }
//            .addOnFailureListener {
//
//            }
//            .await()
        return bookings
    }

    suspend fun createGuideProfile(guide: Guide) : Boolean {
        var isCreated = false
        try {
            guide.email?.let { email ->
                db.collection("guides")
                    .document(email)
                    .set(guide)
                    .await()
                isCreated = true
            }
        } catch (e: Exception) {
            Log.e("CreateGuideProfile", "Error creating guide profile: ${e.message}")
        }
        return isCreated
//        guide.email?.let {
//            db.collection("guides")
//                .document(it)
//                .set(guide)
//                .addOnSuccessListener {
//                    isCreated = true
//                }
//                .await()
//        }
        return isCreated
    }

    suspend fun deleteTour(tourId: String) : Boolean {
        var isDeleted = false
        try {
            db.collection("tours")
                .document(tourId)
                .delete()
                .await()

            Log.d("Tour", "Document successfully deleted: $tourId")
            isDeleted = true
        } catch (e: Exception) {
            Log.e("Tour", "Error deleting document $tourId: ${e.message}")
            isDeleted = false
        }
//        db.collection("tours")
//            .document(tourId)
//            .delete()
//            .addOnSuccessListener {
//                Log.d("Tour", "DocumentSnapshot successfully deleted!")
//                isDeleted = true
//            }
//            .addOnFailureListener {e ->
//                Log.w("Tour", "Error deleting document", e)
//                isDeleted = false
//            }.await()
        return isDeleted
    }

}