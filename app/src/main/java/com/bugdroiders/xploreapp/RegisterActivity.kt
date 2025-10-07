package com.bugdroiders.xploreapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bugdroiders.xploreapp.models.User
import com.bugdroiders.xploreapp.models.UserType
import com.bugdroiders.xploreapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {
    private val view: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(view.root)

        auth = FirebaseAuth.getInstance()

        val userTypes = arrayOf(UserType.TRAVELER, UserType.GUIDE)

        val userTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, userTypes)

        view.dropdownUserType.setAdapter(userTypeAdapter)

        view.btnRegister.setOnClickListener {
            val email = view.etEmail.text.toString()
            val password = view.etPassword.text.toString()
            val fullName = view.etFullName.text.toString()
            val userType = enumValueOf<UserType>(view.dropdownUserType.text.toString())
            view.progressBar.visibility = View.VISIBLE
            createAccount(email, password, fullName, userType)
            view.progressBar.visibility = View.GONE
        }

        view.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

    }

    private fun addUser(user: User) = CoroutineScope(Dispatchers.IO).launch {
        try {
            user.email?.let { Firebase.firestore.collection("users").document(it).set(user).await() }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegisterActivity, "Successfully Added User.", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun createAccount(email: String, password: String, fullName: String, userType: UserType) {
        // [START create_user_with_email]
        Log.d(TAG, "createAccount:$email")
        if (email.isNotEmpty() && password.isNotEmpty() && fullName.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        addUser(User(email, fullName, userType));
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Please complete the required fields", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = "Register"
    }
}