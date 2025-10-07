package com.bugdroiders.xploreapp.data.api

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage

class StoreAPI {
    private val storageRef = Firebase.storage.reference
    private var tourImagesRef: StorageReference? = storageRef.child("images").child("tours")
    private val profileImagesRef: StorageReference = storageRef.child("images").child("profiles")

    fun uploadImage(imageUri: Uri, callback: (String?) -> Unit): String {
        val randomNumber = (10000000..99999999).random().toString()
        val uploadImageRef = imageUri.lastPathSegment?.let {
//            Log.d("Tour", it.toString())
            tourImagesRef?.child(it.plus(randomNumber))
        }
        val uploadTask = uploadImageRef?.putFile(imageUri)

        val urlTask = uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            uploadImageRef.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                callback(downloadUri.toString())
                Log.d("Tour", downloadUri.toString())
            } else {
                // handle failure
            }
        }
        return urlTask.toString()
    }

    fun uploadProfileImage(imageUri: Uri, callback: (String?) -> Unit): String {
        val randomNumber = (10000000..99999999).random().toString()
        val uploadImageRef = imageUri.lastPathSegment?.let {
//            Log.d("Tour", it.toString())
            profileImagesRef.child(it.plus(randomNumber))
        }
        val uploadTask = uploadImageRef?.putFile(imageUri)

        val urlTask = uploadTask?.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            uploadImageRef.downloadUrl
        }?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                callback(downloadUri.toString())
                Log.d("Tour", downloadUri.toString())
            } else {
                // handle failure
            }
        }
        return urlTask.toString()
    }
}