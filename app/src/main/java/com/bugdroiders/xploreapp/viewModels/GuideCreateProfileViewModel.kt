package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Guide
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuideCreateProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val api = FirestoreAPI()

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isProfileCreated: MutableLiveData<Boolean> = MutableLiveData()
    val isFound: MutableLiveData<Boolean> = MutableLiveData()
    val guideInfo: MutableLiveData<Guide?> = MutableLiveData()

    private fun showLoading() {
        isLoading.postValue(true)
    }

    private fun dismissLoading() {
        isLoading.postValue(false)
    }

    fun createProfile(guide: Guide) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            api.createGuideProfile(guide)
            isProfileCreated.postValue(true)
            dismissLoading()
        }
    }

    fun checkIfProfileExists(guideEmail: String) {
        showLoading()
        Log.d("ProfileExists", "It's checking if exists.")
        viewModelScope.launch(Dispatchers.IO) {
            val isExists = api.isGuideExists(guideEmail)
            Log.d("ProfileExists", isExists.toString())
            isFound.postValue(isExists)
            if (isExists) {
                val foundGuide = api.getGuide(guideEmail)
                Log.d("ProfileExists", foundGuide.toString())
                if (foundGuide != null) {
                    guideInfo.postValue(foundGuide.toObject<Guide>())
                }
            }
            dismissLoading()
        }
    }
}