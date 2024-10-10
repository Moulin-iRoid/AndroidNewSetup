package com.example.androidnewsetup.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.androidnewsetup.data.Constants
import com.example.androidnewsetup.data.bean.user.Authentication
import com.example.androidnewsetup.data.bean.user.UserBean
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Singleton

private const val USER_DATA_STORE = "user_data_store"
private const val USER_AUTH_STORE = "user_auth_store"

private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = USER_DATA_STORE)
private val Context.userAuthStore: DataStore<Preferences> by preferencesDataStore(name = USER_AUTH_STORE)

@Singleton
class UserManager(private val context: Context) {

    companion object {
        val USER_DATA = stringPreferencesKey(Constants.Common.USER_DATA)
        val USER_AUTH = stringPreferencesKey(Constants.Common.AUTH_DATA)
    }

    /**
     * user data...
     */
    suspend fun saveUserData(userPreferences: UserBean) {
        val jsonString = Gson().toJson(userPreferences)
        context.userDataStore.edit { preferences ->
            preferences[USER_DATA] = jsonString
        }
    }

    suspend fun getUserJson(): String? {
        val jsonString = context.userDataStore.data.firstOrNull()?.get(USER_DATA)
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        return jsonString
    }

    suspend fun getUserData(): UserBean? {
        val jsonString = context.userDataStore.data.firstOrNull()?.get(USER_DATA)
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        return Gson().fromJson(jsonString, object : TypeToken<UserBean>() {}.type)
    }

    /**
     * user auth...
     */

    suspend fun saveUserAuth(userPreferences: Authentication) {
        val jsonString = Gson().toJson(userPreferences)
        context.userAuthStore.edit { preferences ->
            preferences[USER_AUTH] = jsonString
        }
    }

    suspend fun getUserAuthJson(): String? {
        val jsonString = context.userAuthStore.data.firstOrNull()?.get(USER_AUTH)
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        return jsonString
    }

    suspend fun getUserAuth(): Authentication? {
        val jsonString = context.userAuthStore.data.firstOrNull()?.get(USER_AUTH)
        if (jsonString.isNullOrEmpty()) {
            return null
        }
        return Gson().fromJson(jsonString, object : TypeToken<Authentication>() {}.type)
    }

    suspend fun clearUserData() {
        context.userDataStore.edit {
            it.clear()
        }
    }

    suspend fun clearAuthData() {
        context.userAuthStore.edit {
            it.clear()
        }
    }
}