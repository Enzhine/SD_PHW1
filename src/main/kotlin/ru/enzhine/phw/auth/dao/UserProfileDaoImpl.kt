package ru.enzhine.phw.auth.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.enzhine.phw.auth.dao.exception.UserProfileNotExists
import ru.enzhine.phw.auth.entities.UserProfile
import java.io.FileNotFoundException
import kotlin.io.path.Path

class UserProfileDaoImpl: UserProfileDao {
    companion object {
        private const val WORKDIR = "user_profiles"
    }

    override fun existsUserByLogin(login: String): Boolean {
        val userFile = Path(WORKDIR, "$login.json").toFile()
        return userFile.exists()
    }

    override fun getUserByLogin(login: String): UserProfile {
        val userFile = Path(WORKDIR, "$login.json").toFile()

        if(!existsUserByLogin(login)){
            throw UserProfileNotExists("UserProfile with login $login not exists!")
        }

        return ObjectMapper().readValue(userFile)
    }

    override fun saveUserProfile(user: UserProfile) {
        val userFile = Path(WORKDIR, "${user.login}.json").toFile()

        ObjectMapper().writeValue(userFile, user)
    }
}