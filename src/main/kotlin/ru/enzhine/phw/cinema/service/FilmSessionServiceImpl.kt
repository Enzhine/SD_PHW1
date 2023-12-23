package ru.enzhine.phw.cinema.service

import ru.enzhine.phw.cinema.dao.FilmSessionDao
import ru.enzhine.phw.cinema.entities.Film
import ru.enzhine.phw.cinema.entities.FilmSession
import ru.enzhine.phw.cinema.entities.TheatreState
import java.lang.StringBuilder
import java.time.Instant
import java.time.LocalDateTime

class FilmSessionServiceImpl(private val dao: FilmSessionDao): FilmSessionService {
    companion object{
        const val LOCAL_ROWS: Int = 10
        const val LOCAL_PLACES: Int = 10
    }
    override fun updateFilmSessionStartById(id: Long, start: LocalDateTime): FilmSession {
        val fs = dao.findById(id)
        fs.start = start
        dao.save(fs)
        return fs
    }

    override fun createFilmSession(start: LocalDateTime, film: Film): FilmSession {
        val list = dao.listExisting()
        val newId = if(list.isEmpty()) 0 else (list.maxOf { it.id } + 1)
        val fs = FilmSession(newId, start, film, TheatreState(LOCAL_ROWS, LOCAL_PLACES))
        dao.save(fs)
        return fs
    }

    override fun removeFilmSessionById(id: Long): Boolean {
        return dao.removeById(id)
    }

    override fun saleFilmSessionPlaceById(id: Long, row: Int, place: Int, key: String): Boolean {
        val fs = dao.findById(id)
        if(!fs.theatreState.isPlaceSold(row, place)){
            fs.theatreState.placeSale(row, place, key)
            dao.save(fs)
            return true
        }
        return false
    }

    override fun freeFilmSessionPlaceById(id: Long, row: Int, place: Int, key: String): Boolean {
        val fs = dao.findById(id)
        if(!fs.theatreState.isPlaceSold(row, place)){
            return false
        }
        if(!fs.theatreState.verifyPlace(row, place, key)){
            return false
        }
        fs.theatreState.placeFree(row, place)
        dao.save(fs)
        return true
    }

    override fun registerOnFilmSessionById(id: Long, row: Int, place: Int, key: String): Boolean {
        val fs = dao.findById(id)
        if(fs.isRegistered(row, place)){
            return false
        }
        val res = fs.register(row, place, key)
        if(res){
            dao.save(fs)
        }
        return res
    }

    /**
     * Represents film session as a string, where
     * '#' - means sitting place free; 'X' - means sitting place sold; '@' - means sitting place registered
     */
    override fun representFilmSession(fs: FilmSession): String {
        val sb = StringBuilder()
        sb.append("  ===SCREEN===\n")
        sb.append("\n")
        for (row in 0 until fs.theatreState.rows){
            sb.append(row)
            sb.append(" ")
            for (place in 0 until fs.theatreState.width){
                if(fs.theatreState.isPlaceSold(row, place)){
                    if(fs.isRegistered(row, place)){
                        sb.append("@")
                    }else{
                        sb.append("X")
                    }
                }else{
                    sb.append("#")
                }
                if(place == 2 || place == 6) {
                    sb.append(" ")
                }
            }
            sb.append("\n")
        }
        sb.append("  012 3456 789\n")
        return sb.toString()
    }
}