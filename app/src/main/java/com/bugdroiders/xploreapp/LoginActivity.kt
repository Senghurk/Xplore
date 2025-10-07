package com.bugdroiders.xploreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bugdroiders.xploreapp.data.caches.UserCache
import com.bugdroiders.xploreapp.models.User
import com.bugdroiders.xploreapp.models.UserType
import com.bugdroiders.xploreapp.databinding.ActivityLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class LoginActivity : AppCompatActivity() {
    private val view: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        auth = FirebaseAuth.getInstance()
        val cache = UserCache(this)
        val userInfo = cache.getUserInfo()
//

        if (userInfo != null) {
            userInfo.userType?.let { goHomePage(it) }
        }

        view.btnLogin.setOnClickListener {
            val email = view.etEmail.text.toString()
            val password = view.etPassword.text.toString()

            view.progressBar.visibility = View.VISIBLE
            loginUser(email, password, cache)
            view.progressBar.visibility = View.GONE
        }

        view.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun goHomePage(userType: UserType) {
        Log.d(TAG, "Go to home page is being called $userType")
        if (userType == UserType.TRAVELER) {
            startActivity(Intent(this@LoginActivity, TravelerMainActivity::class.java))
        } else {
            startActivity(Intent(this@LoginActivity, GuideMainActivity::class.java))
        }
    }

    private fun storeUserInfo(user: FirebaseUser, cache: UserCache) = CoroutineScope(Dispatchers.IO).launch {
        Log.d(TAG, "Store User Info is called")
        try {
            user.email?.let {
                val fetchedUser = Firebase.firestore.collection("users")
                    .document(it)
                    .get()
                    .await()

                val userInfo = fetchedUser.toObject(User::class.java)

                if (userInfo != null) {
                    cache.setUserCache(userInfo)
                    userInfo.userType?.let { it1 -> goHomePage(it1) }
                }

            }
        } catch (e: Exception) {
            Log.d(TAG, "Error fetching user data. ${e.message}")
        }
    }

    private fun loginUser(email: String, password: String, cache: UserCache) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Login Successful!", Toast.LENGTH_SHORT)
                            .show()
                        auth.currentUser?.let {
                            storeUserInfo(it, cache)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = "Login"
    }
}