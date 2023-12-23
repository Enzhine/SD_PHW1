package ru.enzhine.phw.cinema.service

import ru.enzhine.phw.cinema.dao.FilmDao
import ru.enzhine.phw.cinema.dao.FilmSessionDao
import ru.enzhine.phw.cinema.entities.Film

class FilmServiceImpl(private val fDao: FilmDao, private val fsDao: FilmSessionDao): FilmService {
    override fun updateFilmTitleById(id: Long, title: String): Film {
        val f = fDao.findById(id)
        f.title = title
        fDao.save(f)
        return f
    }

    override fun updateFilmDescriptionById(id: Long, description: String): Film {
        val f = fDao.findById(id)
        f.description = description
        fDao.save(f)
        return f
    }

    override fun updateFilmCostById(id: Long, cost: Int): Film {
        val f = fDao.findById(id)
        f.cost = cost
        fDao.save(f)
        return f
    }

    override fun updateFilmLengthById(id: Long, length: Long): Film {
        val f = fDao.findById(id)
        f.length = length
        fDao.save(f)
        return f
    }

    override fun createFilm(title: String, description: String, length: Long, cost: Int): Film {
        val list = fDao.listExisting()
        val newId = if(list.isEmpty()) 0 else (list.maxOf { it.id } + 1)
        val f = Film(newId, title, description, length, cost)
        fDao.save(f)
        return f
    }

    /**
     * Deletes film with film-sessions related to such film
     */
    override fun removeFilmById(id: Long): Boolean {
        val flag = fDao.removeById(id)
        if(flag){
            val filmSessions = fsDao.listExisting().filter { it.film.id == id }
            if(filmSessions.isNotEmpty()){
                for (fs in filmSessions){
                    fsDao.removeById(fs.id)
                }
            }
        }
        return flag
    }
}