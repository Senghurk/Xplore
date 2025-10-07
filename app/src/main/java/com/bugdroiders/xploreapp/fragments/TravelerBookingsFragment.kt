package com.bugdroiders.xploreapp.fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugdroiders.xploreapp.GiveReviewActivity
import com.bugdroiders.xploreapp.adapters.RecyclerViewBookingAdapter
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.FragmentTravelerBookingsBinding
import com.bugdroiders.xploreapp.models.Booking
import com.bugdroiders.xploreapp.viewModels.BookingViewModel
import com.google.firebase.Timestamp
import java.util.Date

class TravelerBookingsFragment : Fragment() {
    private lateinit var view : FragmentTravelerBookingsBinding

    private val viewModel: BookingViewModel by viewModels<BookingViewModel>()
    private val bookings: MutableList<Booking> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentTravelerBookingsBinding.inflate(inflater, container, false)
        val cache = UserCache(requireContext())
        val userInfo = cache.getUserInfo()

        if (userInfo != null) {
            userInfo.email?.let { viewModel.getBookings(it, true) }
        }

        return view.root
    }

    override fun onViewCreated(view2: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view2, savedInstanceState)

        val bookingsAdapter = RecyclerViewBookingAdapter(requireContext(), bookings, true)
        view.rvBookings.adapter = bookingsAdapter
        view.rvBookings.layoutManager = LinearLayoutManager(requireContext())

        bookingsAdapter.onItemClick = {
            val intent = Intent(requireContext(), GiveReviewActivity::class.java)
            intent.putExtra("booking", it)
            startActivity(intent)
        }



        viewModel.bookings.observe(viewLifecycleOwner) {
            bookings.clear()
            bookings.addAll(it)
            bookingsAdapter?.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                view.pbTravelerBooking.visibility = View.VISIBLE
            } else {
                view.pbTravelerBooking.visibility = View.GONE
            }
        }
    }
}