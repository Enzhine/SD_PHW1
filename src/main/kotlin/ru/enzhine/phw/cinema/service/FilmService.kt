package ru.enzhine.phw.cinema.service

import ru.enzhine.phw.cinema.entities.Film

interface FilmService {
    /**
     * updates Film title property to Film with such id
     * @return Film instance with updated title property
     */
    fun updateFilmTitleById(id: Long, title: String): Film

    /**
     * updates Film description property to Film with such id
     * @return Film instance with updated description property
     */
    fun updateFilmDescriptionById(id: Long, description: String): Film

    /**
     * updates Film cost property to Film with such id
     * @return Film instance with updated cost property
     */
    fun updateFilmCostById(id: Long, cost: Int): Film

    /**
     * updates Film length property to Film with such id
     * @return Film instance with updated length property
     */
    fun updateFilmLengthById(id: Long, length: Long): Film

    /**
     * creates Film with provided properties
     * @return new Film instance
     */
    fun createFilm(title: String, description: String, length: Long, cost: Int): Film

    /**
     * deletes all related FilmSessions and Film with such id
     * @return whether Film with such id was successfully deleted
     */
    fun removeFilmById(id: Long): Boolean
}