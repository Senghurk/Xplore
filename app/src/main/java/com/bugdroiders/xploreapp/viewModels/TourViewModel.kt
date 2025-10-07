package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.FirestoreAPI
import com.bugdroiders.xploreapp.models.Guide
import com.bugdroiders.xploreapp.models.Tour
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TourViewModel(app:Application): AndroidViewModel(app) {
    private val api = FirestoreAPI()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData()

    val tours : MutableLiveData<List<Tour>> = MutableLiveData()

    val guides: MutableLiveData<List<Guide>> = MutableLiveData()

    var tabSelectedIndex = 0
    val query = MutableLiveData<String>()

    fun getTours() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            val toursList = api.getToursSnapshot()
            tours.postValue(toursList)
        }
        dismissLoading()
    }

    fun getGuides() {
        showLoading()
        viewModelScope.launch(Dispatchers.IO) {
            val guideList = api.getGuidesSnapshot()
            guides.postValue(guideList)
        }
        dismissLoading()
    }

    fun setQuery(newQuery: String) {
        query.postValue(newQuery)
    }

    fun setPosition(tabIndex: Int) {
        tabSelectedIndex = tabIndex
    }

    private fun showLoading() {
        isLoading.postValue(true)
    }

    private fun dismissLoading() {
        isLoading.postValue(false)
    }
}