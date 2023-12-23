package ru.enzhine.phw.auth.dao

import ru.enzhine.phw.auth.entities.UserProfile

interface UserProfileDao {
    /**
     * @return whether the user with such login exists
     */
    fun existsUserByLogin(login: String): Boolean

    /**
     * @return UserProfile instance, existing till the last moment
     */
    fun getUserByLogin(login: String): UserProfile

    /**
     * saves UserProfile instance
     */
    fun saveUserProfile(user: UserProfile)
}