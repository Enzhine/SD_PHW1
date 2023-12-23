package ru.enzhine.phw.cinema.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.enzhine.phw.cinema.entities.Film
import java.io.FileNotFoundException
import kotlin.io.path.Path

class FilmDaoImpl: FilmDao {
    companion object {
        private const val WORKDIR = "films"
    }

    override fun findById(id: Long): Film {
        val filmFile = Path(WORKDIR, "$id.json").toFile()

        if(!filmFile.exists()){
            throw FileNotFoundException("Film with id $id not exists!")
        }

        return ObjectMapper().readValue(filmFile)
    }

    override fun removeById(id: Long): Boolean {
        val filmFile = Path(WORKDIR, "$id.json").toFile()

        if(!filmFile.exists()){
            return false
        }

        filmFile.delete()
        return true
    }

    override fun save(film: Film) {
        val filmFile = Path(WORKDIR, "${film.id}.json").toFile()

        ObjectMapper().writeValue(filmFile, film)
    }

    override fun listExisting(): List<Film> {
        val filmsDir = Path(WORKDIR).toFile()

        if(!filmsDir.exists()){
            filmsDir.mkdir()
        }
        val list = filmsDir.listFiles() ?: return emptyList()

        val om = ObjectMapper()
        return list.filter { it.extension == "json" }.map {om.readValue(it)}
    }
}