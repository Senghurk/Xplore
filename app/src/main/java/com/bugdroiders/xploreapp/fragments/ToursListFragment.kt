package com.bugdroiders.xploreapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugdroiders.xploreapp.TourDetailActivity
import com.bugdroiders.xploreapp.adapters.RecyclerViewTourAdapter
import com.bugdroiders.xploreapp.databinding.FragmentToursListBinding
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.viewModels.TourViewModel

class ToursListFragment : Fragment() {

    private lateinit var view: FragmentToursListBinding
    private val viewModel: TourViewModel by activityViewModels<TourViewModel>()

    private val tours: MutableList<Tour> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentToursListBinding.inflate(inflater, container, false)

        return view.root
    }

    override fun onViewCreated(view2: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view2, savedInstanceState)
        initViews()
        observeViews()

        viewModel.getTours()

        view.swipeTours.setOnRefreshListener {
            viewModel.getTours()
        }
    }

    private fun initViews() {
        val tourAdapter = RecyclerViewTourAdapter(requireContext(), tours)
        view.rcViewTours.adapter = tourAdapter
        view.rcViewTours.layoutManager = LinearLayoutManager(requireContext())
        tourAdapter.onItemClick = {
            val intent = Intent(requireContext(), TourDetailActivity::class.java)
            intent.putExtra("tour", it)
            startActivity(intent)
        }

    }

    private fun observeViews() {
        viewModel.tours.observe(viewLifecycleOwner) {
            tours.clear()
            tours.addAll(it)
            view.rcViewTours.adapter?.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            view.swipeTours.isRefreshing = it
        }

        viewModel.query.observe(viewLifecycleOwner) { query ->
            val isTourEvent = viewModel.tabSelectedIndex == 0
            if (isTourEvent && query.isNotEmpty()) {
                val newList = tours.filter { tour ->
                    tour.title?.lowercase()?.contains(query.lowercase()) ?: false
                }
                tours.clear()
                tours.addAll(newList)
            } else {
                tours.clear()
                tours.addAll(viewModel.tours.value.orEmpty())
            }
            //TODO Improvement

            view.rcViewTours.adapter?.notifyDataSetChanged()
        }
    }
}