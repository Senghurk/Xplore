package com.bugdroiders.xploreapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import com.bugdroiders.xploreapp.GuideCreateProfileActivity
import com.bugdroiders.xploreapp.LoginActivity
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.databinding.FragmentTravelerProfileBinding
import com.bugdroiders.xploreapp.models.UserType
import com.bugdroiders.xploreapp.viewModels.ProfileViewModel

class TravelerProfileFragment : Fragment() {
    private lateinit var view: FragmentTravelerProfileBinding

    private val viewModel : ProfileViewModel by activityViewModels<ProfileViewModel>()

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {uri ->
        Log.d("Tour", uri.toString())
        if (uri != null) {
            view.ivProfile.load(
                uri
            )
            viewModel.updateProfilePic(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentTravelerProfileBinding.inflate(inflater, container, false)
        val cache = UserCache(requireContext())
        val userInfo = cache.getUserInfo()

        if (userInfo != null) {
            view.tvName.text = userInfo.fullName
            view.tvUserType.text = userInfo.userType.toString()

            if (userInfo.imageUrl != null) {
                view.ivProfile.load(
                    userInfo.imageUrl
                )
            }

            if (userInfo.userType == UserType.GUIDE) {
                view.cardProfile.visibility = View.VISIBLE
            } else {
                view.cardProfile.visibility = View.GONE
            }
        }

        view.ivProfile.setOnClickListener {
            pickImage()
        }

        view.btnSignOut.setOnClickListener {
            cache.removeUserCache()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        view.cardProfile.setOnClickListener {
            val intent = Intent(requireContext(), GuideCreateProfileActivity::class.java)
            startActivity(intent)
        }


        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeView()
    }

    private fun observeView() {
        viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun pickImage() {
        pickPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}