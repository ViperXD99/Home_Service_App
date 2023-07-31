package lk.nibm.hireupapp.common

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

    // Function to save the user's sign-in status and any relevant data
    fun saveUserLoggedIn(isLoggedIn: Boolean, userId: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("userId", userId)
        editor.apply()
    }

    // Function to check if the user is already signed in
    fun isUserLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    // Function to get the user's ID after sign-in
    fun getUserId(): String? {
        return sharedPreferences.getString("userId", null)
    }

    // Function to clear user data and sign out
    fun clearUserData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}