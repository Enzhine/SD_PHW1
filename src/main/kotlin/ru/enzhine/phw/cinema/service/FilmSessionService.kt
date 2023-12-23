package ru.enzhine.phw.cinema.service

import ru.enzhine.phw.cinema.entities.Film
import ru.enzhine.phw.cinema.entities.FilmSession
import java.time.Instant
import java.time.LocalDateTime

interface FilmSessionService {
    /**
     * updates FilmSession start property to FilmSession with such id
     * @return FilmSession instance with updated start property
     */
    fun updateFilmSessionStartById(id: Long, start: LocalDateTime): FilmSession

    /**
     * creates FilmSession with provided properties
     * @return new FilmSession instance
     */
    fun createFilmSession(start: LocalDateTime, film: Film): FilmSession

    /**
     * @return whether FilmSession with such id was successfully deleted
     */
    fun removeFilmSessionById(id: Long): Boolean

    /**
     * set sitting-place at provided row and place sold with provided key
     * @return whether sitting-place sale was successful
     */
    fun saleFilmSessionPlaceById(id: Long, row: Int, place: Int, key: String): Boolean

    /**
     * set sitting-place at provided row and place free if provided key correct
     * @return whether sitting-place free was successful
     */
    fun freeFilmSessionPlaceById(id: Long, row: Int, place: Int, key: String): Boolean

    /**
     * set sitting-place at provided row and place registered if provided key correct
     * @return whether sitting-place register was successful
     */
    fun registerOnFilmSessionById(id: Long, row: Int, place: Int, key: String): Boolean

    /**
     * @return FilmSession sitting-places string representation
     */
    fun representFilmSession(fs: FilmSession): String
}