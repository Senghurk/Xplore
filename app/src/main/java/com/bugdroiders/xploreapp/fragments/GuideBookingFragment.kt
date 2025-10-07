package com.bugdroiders.xploreapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugdroiders.xploreapp.adapters.RecyclerViewBookingAdapter
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.FragmentGuideBookingsBinding
import com.bugdroiders.xploreapp.models.Booking
import com.bugdroiders.xploreapp.viewModels.BookingViewModel

class GuideBookingFragment : Fragment() {
    private lateinit var view: FragmentGuideBookingsBinding

    private val viewModel: BookingViewModel by viewModels<BookingViewModel>()
    private val bookings: MutableList<Booking> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentGuideBookingsBinding.inflate(inflater, container, false)
        val cache = UserCache(requireContext())
        val userInfo = cache.getUserInfo()

        if (userInfo != null) {
            userInfo.email?.let { viewModel.getBookings(it, false) }
        }
        return view.root
    }

    override fun onViewCreated(view2: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view2, savedInstanceState)

        val bookingAdapter = RecyclerViewBookingAdapter(requireContext(), bookings, false)
        view.rvGuideBookings.adapter = bookingAdapter
        view.rvGuideBookings.layoutManager = LinearLayoutManager(requireContext())

        viewModel.bookings.observe(viewLifecycleOwner) {
            bookings.clear()
            bookings.addAll(it)
            bookingAdapter?.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                view.pbGuideBookings.visibility = View.VISIBLE
            } else {
                view.pbGuideBookings.visibility = View.GONE
            }
        }
    }
}