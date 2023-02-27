package com.example.agriappadminpanel.data

import com.example.agriappadminpanel.data.model.LoggedInUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(username: String, password: String): Result<LoggedInUser> {
        if (username == "Admin" && password =="Admin@123"){
            try {
                val trueUser = LoggedInUser(password, username)
                return Result.Success(trueUser)
            } catch (e: Throwable) {
                return Result.Error(IOException("Error logging in", e))
            }
        }else
        {
            return Result.Error(IOException("Error logging in"))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}