package ru.enzhine.phw.cinema.dao

import ru.enzhine.phw.cinema.entities.Film

interface FilmDao {
    /**
     * @return Film instance with such id, existing till the last moment
     */
    fun findById(id: Long): Film

    /**
     * @return whether Film with such id was successfully deleted
     */
    fun removeById(id: Long): Boolean

    /**
     * saves Film instance
     */
    fun save(film: Film)

    /**
     * provides list of all Film instances, existing till the last moment
     */
    fun listExisting(): List<Film>
}