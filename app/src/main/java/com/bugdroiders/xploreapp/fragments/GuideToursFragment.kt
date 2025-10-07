package com.bugdroiders.xploreapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugdroiders.xploreapp.GuideCreateTourActivity
import com.bugdroiders.xploreapp.GuideTourDetailActivity
import com.bugdroiders.xploreapp.TourDetailActivity
import com.bugdroiders.xploreapp.adapters.GuideTourListAdapter
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.FragmentGuideToursBinding
import com.bugdroiders.xploreapp.models.Tour
import com.bugdroiders.xploreapp.viewModels.GuideMainViewModel

class GuideToursFragment : Fragment() {
    private lateinit var view : FragmentGuideToursBinding

    private val viewModel: GuideMainViewModel by activityViewModels<GuideMainViewModel>()

    private val tours: MutableList<Tour> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentGuideToursBinding.inflate(inflater, container, false)

        view.fab.setOnClickListener {
            val intent = Intent(requireContext(), GuideCreateTourActivity::class.java)
            startActivity(intent)
        }
        return view.root
    }

    override fun onViewCreated(view2: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view2, savedInstanceState)

        initView()
        observeView()

        val userCache = UserCache(requireContext())
        userCache.getUserInfo()?.email?.let { viewModel.getTours(it) }
    }

    private fun initView() {
        val toursAdapter = GuideTourListAdapter(requireContext(), tours)
        view.rvGuideTours.adapter = toursAdapter
        view.rvGuideTours.layoutManager = LinearLayoutManager(requireContext())

        toursAdapter.onItemClick = {
            val intent = Intent(requireContext(), GuideTourDetailActivity::class.java)
            intent.putExtra("tour", it)
            startActivity(intent)
        }
    }

    private fun observeView() {
        viewModel.tours.observe(viewLifecycleOwner) {
            tours.clear()
            tours.addAll(it)
            view.rvGuideTours.adapter?.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                view.pbGuideTours.visibility = View.VISIBLE
            } else {
                view.pbGuideTours.visibility = View.GONE
            }
        }
    }
}