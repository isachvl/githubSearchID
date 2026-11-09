package com.example.githubsee

import android.content.Context

class KeyValueStorage(context : Context) {
    private val preferences = context.getSharedPreferences(
        "GitHubSEE",
        Context.MODE_PRIVATE

    )
    var authToken: String?
        get() =preferences.getString("auth_token",null)
        set(value){
            preferences.edit().putString("auth_token",value).apply()
        }



}