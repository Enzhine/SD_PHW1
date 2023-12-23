package ru.enzhine.phw.cinema.dao

import ru.enzhine.phw.cinema.entities.FilmSession

interface FilmSessionDao {
    /**
     * @return FilmSession instance with such id, existing till the last moment
     */
    fun findById(id: Long): FilmSession

    /**
     * @return whether FilmSession with such id was successfully deleted
     */
    fun removeById(id: Long): Boolean

    /**
     * saves FilmSession instance
     */
    fun save(filmSession: FilmSession)

    /**
     * provides list of all FilmSession instances, existing till the last moment
     */
    fun listExisting(): List<FilmSession>
}