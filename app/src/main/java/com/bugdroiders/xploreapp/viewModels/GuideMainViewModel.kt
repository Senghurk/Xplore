package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Tour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuideMainViewModel(app: Application) : AndroidViewModel(app) {
    private val api = FirestoreAPI()


    val tours: MutableLiveData<List<Tour>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun getTours(guideEmail: String) {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            val toursList = api.getTours(guideEmail)
            tours.postValue(toursList)
            dismissLoading()
        }
    }

    fun showLoading() {
        isLoading.postValue(true);
    }

    fun dismissLoading() {
        isLoading.postValue(false)
    }
}