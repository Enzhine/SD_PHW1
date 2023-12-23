package ru.enzhine.phw.auth.entities

import com.fasterxml.jackson.annotation.JsonProperty

/**
 *
 */
data class UserProfile(
    @JsonProperty("login") val login: String,
    @JsonProperty("pwd") val pwd: String
    )