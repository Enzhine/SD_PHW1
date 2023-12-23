package ru.enzhine.phw.cui

import ru.enzhine.phw.cinema.dao.FilmDao
import ru.enzhine.phw.cinema.dao.FilmSessionDao
import ru.enzhine.phw.cinema.entities.Film
import ru.enzhine.phw.cinema.entities.FilmSession
import ru.enzhine.phw.cinema.service.FilmSessionService
import ru.enzhine.phw.cui.base.ActingWidget
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class FilmSessionWidget(private val fsId: Long, private val dao: FilmSessionDao, private val service: FilmSessionService): ActingWidget {
    companion object {
        private fun isIntersectingWithOther(film: Film, start: LocalDateTime, dao: FilmSessionDao): Boolean {
            return dao.listExisting().any {(it.start <= start && start <= it.start.plusSeconds(film.length)) || (it.start <= start && start.plusSeconds(film.length) <= it.start.plusSeconds(film.length))}
        }
        fun createNewFilmSession(filmDao: FilmDao, filmSessionDao: FilmSessionDao, service: FilmSessionService): FilmSession{
            println("[Creating new film-session]")
            println(" Existing films:")
            val films = filmDao.listExisting()
            for(film in films){
                println(" #${film.id} {title=${film.title};cost=${film.cost};...}")
            }
            var film: Film
            while(true){
                println(" Please, input film id")
                try{
                    film = filmDao.findById(FilmWidget.readLong(">> "))
                    break
                }catch (_: FileNotFoundException){
                    println(" FAILURE: Wrong film id!")
                    Thread.sleep(1000)
                }
            }
            var start: LocalDateTime
            while(true){
                println(" Please, input date-time (yyyy-MM-dd HH:mm:ss)")
                print(">> ")
                try{
                    start = LocalDateTime.parse(readln(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    if(isIntersectingWithOther(film, start, filmSessionDao)){
                        println(" ERROR: film-session interval overlaps with other film-session!")
                        continue
                    }
                    break
                }catch (_: DateTimeParseException){
                    println(" FAILURE: Wrong input")
                    Thread.sleep(1000)
                }
            }
            return service.createFilmSession(start, film)
        }
    }

    override val openTitle: String
        get() = "Edit film-session with id=$fsId"

    override fun run(): Boolean {
        var session = dao.findById(fsId)
        while(true) {
            println("[Session #${session.id}]")
            println(" | start->${session.start}")
            println(" | filmId->${session.film.id}")
            println(service.representFilmSession(session))
            println("[Please, choose option]")
            println(" #0 Update start")
            println(" #1 Remove film-session")
            println(" #2 Back")
            print(">> ")
            when(readln()){
                "0" -> {
                    while(true){
                        println(" Please, input date-time (yyyy-MM-dd HH:mm:ss)")
                        print(">> ")
                        try{
                            val newVal = LocalDateTime.parse(readln(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                            if(isIntersectingWithOther(session.film, newVal, dao)){
                                println(" ERROR: film-session interval overlaps with other film-session!")
                                continue
                            }
                            session = service.updateFilmSessionStartById(fsId, newVal)
                            break
                        }catch (_: DateTimeParseException){
                            println(" FAILURE: Wrong input")
                            Thread.sleep(1000)
                        }
                    }
                    println(" SUCCESS")
                    Thread.sleep(1000)
                }
                "1" -> {
                    println(" If you sure, please, type 'YES'")
                    print(">> ")
                    val answer = readln()
                    if(answer == "YES"){
                        if(service.removeFilmSessionById(fsId)){
                            println(" SUCCESS")
                            Thread.sleep(1000)
                            return true
                        }else{
                            println(" FAILED: something went wrong.")
                        }
                    }else{
                        println(" CANCELLED")
                    }
                    Thread.sleep(1000)
                }
                "2" -> {
                    return true
                }
                else -> {
                    println(" FAILED: Wrong action!")
                    Thread.sleep(1000)
                }
            }
        }
    }
}