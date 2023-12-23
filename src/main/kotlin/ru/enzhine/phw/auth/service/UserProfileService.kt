package ru.enzhine.phw.auth.service

interface UserProfileService {
    /**
     * @return whether login and password are both correct
     */
    fun authorize(login: String, password: String): Boolean

    /**
     * @return whether UserProfile with such login and password was created
     */
    fun register(login: String, password: String): Boolean
}