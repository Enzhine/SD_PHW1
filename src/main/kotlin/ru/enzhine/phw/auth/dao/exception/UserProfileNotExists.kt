package ru.enzhine.phw.auth.dao.exception

import java.io.FileNotFoundException

class UserProfileNotExists(private val msg: String) : FileNotFoundException(msg)