package com.bugdroiders.xploreapp.data.caches

import android.content.Context
import com.bugdroiders.xploreapp.models.User
import com.google.gson.Gson

class UserCache(private val context: Context) {
    private val cache = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val gson = Gson()
    fun isLogin(): Boolean {
        return cache.contains("userInfo")
    }

    fun getUserInfo(): User? {
        val userValue = cache.getString("userInfo", null)

        return userValue?.let {
            gson.fromJson(it, User::class.java)
        }
    }

    fun setUserCache(user: User) {
        cache.edit().apply {
            putString("userInfo", gson.toJson(user))
            apply()
        }
    }

    fun removeUserCache() {
        cache.edit().remove("userInfo").apply()
    }
}