package lk.nibm.hireupapp.common

import lk.nibm.hireupapp.model.User

object UserDataManager {
    private var user: User? = null
    fun setUser(userData: User){
        user = userData
    }
    fun getUser(): User?{
        return user
    }
}