package ru.enzhine.phw.auth.service

import ru.enzhine.phw.auth.dao.UserProfileDao
import ru.enzhine.phw.auth.dao.exception.UserProfileNotExists
import ru.enzhine.phw.auth.entities.UserProfile
import java.nio.charset.Charset
import java.security.MessageDigest

class UserProfileServiceImpl(private val dao: UserProfileDao): UserProfileService {
    private fun mkSHA1(password: String): String{
        val md = MessageDigest.getInstance("SHA-1")
        md.update(password.toByteArray(Charset.defaultCharset()))
        val ba = md.digest()
        return ba.joinToString("") { "%02x".format(it) }
    }
    override fun authorize(login: String, password: String): Boolean {
        return try {
            val userProfile = dao.getUserByLogin(login)
            userProfile.pwd == mkSHA1(password)
        }catch (ex: UserProfileNotExists){
            false
        }
    }

    override fun register(login: String, password: String): Boolean {
        if(dao.existsUserByLogin(login)){
            return false
        }
        val userProfile = UserProfile(login, mkSHA1(password))
        dao.saveUserProfile(userProfile)
        return true
    }
}