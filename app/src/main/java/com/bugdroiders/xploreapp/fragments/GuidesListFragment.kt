package com.bugdroiders.xploreapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bugdroiders.xploreapp.TravelerGuideDetailActivity
import com.bugdroiders.xploreapp.adapters.RecyclerViewGuidesListAdapter
import com.bugdroiders.xploreapp.databinding.FragmentGuidesListBinding
import com.bugdroiders.xploreapp.databinding.FragmentToursListBinding
import com.bugdroiders.xploreapp.models.Guide
import com.bugdroiders.xploreapp.viewModels.TourViewModel

class GuidesListFragment : Fragment() {
    private lateinit var view: FragmentGuidesListBinding
    private val viewModel: TourViewModel by activityViewModels<TourViewModel>()

    private val guides: MutableList<Guide> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentGuidesListBinding.inflate(inflater, container, false)


        return view.root
    }

    override fun onViewCreated(view2: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view2, savedInstanceState)
        initViews()

        viewModel.guides.observe(viewLifecycleOwner) {
            guides.clear()
            guides.addAll(it)
            view.rvGuides.adapter?.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            view.swipeGuides.isRefreshing = it
        }

        viewModel.query.observe(viewLifecycleOwner) {query ->
            val isGuideEvent = viewModel.tabSelectedIndex == 1
            if (isGuideEvent && query.isNotEmpty()) {
                val newGuideList = guides.filter { guide ->
                    val locationMatch = guide.location?.lowercase()?.contains(query.lowercase()) ?: false
                    val nameMatch = guide.name?.lowercase()?.contains(query.lowercase()) ?: false
                    locationMatch || nameMatch
                }
                guides.clear()
                guides.addAll(newGuideList)
            } else {
                guides.clear()
                guides.addAll(viewModel.guides.value.orEmpty())
            }
            view.rvGuides.adapter?.notifyDataSetChanged()
        }

        view.swipeGuides.setOnRefreshListener {
            viewModel.getGuides()
        }

        viewModel.getGuides()
    }

    private fun initViews() {
        val guideAdapter = RecyclerViewGuidesListAdapter(requireContext(), guides)
        view.rvGuides.adapter = guideAdapter
        view.rvGuides.layoutManager = LinearLayoutManager(requireContext())

        guideAdapter.onItemClick = {
            val intent = Intent(requireContext(), TravelerGuideDetailActivity::class.java)
            intent.putExtra("guide", it)
            startActivity(intent)
        }
    }
}