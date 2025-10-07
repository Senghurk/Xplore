package com.bugdroiders.xploreapp.viewModels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bugdroiders.xploreapp.data.api.StoreAPI
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.models.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val storageAPI = StoreAPI()
    private val context = getApplication<Application>().applicationContext
    val message: MutableLiveData<String> = MutableLiveData()

    fun updateProfilePic(imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            storageAPI.uploadProfileImage(imageUri) {downloadUrl ->
                if (downloadUrl != null) {
                    val cache = UserCache(context)
                    val userInfo = cache.getUserInfo()
                    val db = Firebase.firestore
                    if (userInfo != null) {
                        userInfo.email?.let { db.collection("users").document(it)
                            .update(
                                mapOf(
                                    "imageUrl" to downloadUrl
                                )
                            )
                            .addOnSuccessListener {
                                message.postValue("Upload profile image successfully.")
                                val updateUser = User(
                                    email = userInfo.email,
                                    userType = userInfo.userType,
                                    fullName = userInfo.fullName,
                                    imageUrl = downloadUrl
                                )
                                cache.setUserCache(updateUser)
                            }
                            .addOnFailureListener {exception ->
                                message.postValue("Error uploading profile image ${exception.message}")
                            }
                        }
                    }
                } else {
                    message.postValue("Upload Image failed.")
                }
            }
        }
    }
}