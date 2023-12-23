package ru.enzhine.phw.cinema.entities

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.LocalDateTime
import java.util.LinkedList

data class FilmSession(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("start")
    var start: LocalDateTime,
    @JsonProperty("film")
    val film: Film,
    @JsonProperty("theatreState")
    val theatreState: TheatreState
    ){
    @JsonProperty("register")
    val register: MutableList<Pair<Int,Int>> = LinkedList()

    fun isGoing(timeNow: LocalDateTime): Boolean{
        return timeNow >= start && timeNow < start.plusSeconds(film.length)
    }
    fun isRegistered(row: Int, place: Int): Boolean {
        return register.any { it.first == row && it.second == place }
    }
    fun register(row: Int, place: Int, key: String): Boolean {
        if(!theatreState.isPlaceSold(row, place)){
            return false
        }
        if(!theatreState.verifyPlace(row, place, key)){
            return false
        }
        register.add(Pair(row, place))
        return true
    }
}