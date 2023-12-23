package ru.enzhine.phw.cinema.dao

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import ru.enzhine.phw.cinema.entities.FilmSession
import java.io.FileNotFoundException
import kotlin.io.path.Path

class FilmSessionDaoImpl: FilmSessionDao {
    companion object {
        private const val WORKDIR = "film_sessions"
    }

    override fun findById(id: Long): FilmSession {
        val filmSessionFile = Path(WORKDIR, "$id.json").toFile()

        if(!filmSessionFile.exists()){
            throw FileNotFoundException("FilmSession with id $id not exists!")
        }
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        return mapper.readValue(filmSessionFile)
    }

    override fun removeById(id: Long): Boolean {
        val filmSessionFile = Path(WORKDIR, "$id.json").toFile()

        if(!filmSessionFile.exists()){
            return false
        }

        filmSessionFile.delete()
        return true
    }

    override fun save(filmSession: FilmSession) {
        val filmFile = Path(WORKDIR, "${filmSession.id}.json").toFile()
        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        mapper.writeValue(filmFile, filmSession)
    }

    override fun listExisting(): List<FilmSession> {
        val filmSessionsDir = Path(WORKDIR).toFile()

        if(!filmSessionsDir.exists()){
            filmSessionsDir.mkdir()
        }
        val list = filmSessionsDir.listFiles() ?: return emptyList()

        val mapper = ObjectMapper()
        mapper.registerModule(JavaTimeModule())
        mapper.registerKotlinModule()
        return list.filter { it.extension == "json" }.map {mapper.readValue(it)}
    }
}