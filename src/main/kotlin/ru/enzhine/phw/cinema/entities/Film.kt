package ru.enzhine.phw.cinema.entities

import com.fasterxml.jackson.annotation.JsonProperty

data class Film(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("title")
    var title: String,
    @JsonProperty("description")
    var description: String,
    @JsonProperty("length")
    var length: Long,
    @JsonProperty("cost")
    var cost: Int
    )