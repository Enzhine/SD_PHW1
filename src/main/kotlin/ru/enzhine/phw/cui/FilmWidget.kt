package ru.enzhine.phw.cui

import ru.enzhine.phw.cinema.dao.FilmDao
import ru.enzhine.phw.cinema.dao.FilmSessionDao
import ru.enzhine.phw.cinema.entities.Film
import ru.enzhine.phw.cinema.service.FilmService
import ru.enzhine.phw.cui.base.ActingWidget

class FilmWidget(private val filmId: Long, private val dao: FilmDao, private val service: FilmService, private val sessionDao: FilmSessionDao): ActingWidget {
    companion object{
        fun readLong(preInp: String): Long {
            while(true){
                print(preInp)
                try {
                    val read = readln().toLong()
                    if(read < 0){
                        println(" WRONG INPUT: negative number")
                        continue
                    }
                    return read
                }catch (_: NumberFormatException){
                    println(" WRONG INPUT")
                }
            }
        }
        fun readInt(preInp: String): Int {
            while(true){
                print(preInp)
                try {
                    val read = readln().toInt()
                    if(read < 0){
                        println(" WRONG INPUT: negative number")
                        continue
                    }
                    return read
                }catch (_: NumberFormatException){
                    println(" WRONG INPUT")
                }
            }
        }
        fun createNewFilm(filmService: FilmService): Film{
            println("[Creating new film]")
            println(" Please, input title")
            print(">> ")
            val title = readln()
            println(" Please, input description")
            print(">> ")
            val descr = readln()
            println(" Please, input film length (seconds)")
            val len = readLong(">> ")
            println(" Please, input film cost (integer)")
            val cost = readInt(">> ")
            return filmService.createFilm(title, descr, len, cost)
        }
    }

    override val openTitle: String
        get() = "Edit film with id=$filmId"

    override fun run(): Boolean {
        var film = dao.findById(filmId)
        while(true){
            println("[Film #${film.id}]")
            println(" | title->${film.title}")
            println(" | description->${film.description}")
            println(" | length(secons)->${film.length}")
            println(" | cost->${film.cost}")
            println("[Please, choose option]")
            println(" #0 Update title")
            println(" #1 Update description")
            println(" #2 Update length")
            println(" #3 Update cost")
            println(" #4 Remove film")
            println(" #5 Back")
            print(">> ")
            when (readln()) {
                "0" -> {
                    println(" Please, input title")
                    print(">> ")
                    val newVal = readln()
                    film = service.updateFilmTitleById(filmId, newVal)
                    println(" SUCCESS")
                    Thread.sleep(1000)
                }
                "1" -> {
                    println(" Please, input description")
                    print(">> ")
                    val newVal = readln()
                    film = service.updateFilmDescriptionById(filmId, newVal)
                    println(" SUCCESS")
                    Thread.sleep(1000)
                }
                "2" -> {
                    if(sessionDao.listExisting().any {it.film.id == filmId}){
                        println(" FAILURE: There are some film-sessions related to selected film!")
                        println("          In order to change the film length, please, remove all")
                        println("          related film sessions!")
                        Thread.sleep(1000)
                    }else{
                        println(" Please, input length (seconds)")
                        val newVal = readLong(">> ")
                        film = service.updateFilmLengthById(filmId, newVal)
                        println(" SUCCESS")
                        Thread.sleep(1000)
                    }
                }
                "3" -> {
                    println(" Please, input cost (integer)")
                    val newVal = readInt(">> ")
                    film = service.updateFilmCostById(filmId, newVal)
                    println(" SUCCESS")
                    Thread.sleep(1000)
                }
                "4" -> {
                    println(" ATTENTION! It will remove all related Film-Sessions!")
                    println(" If you sure, please, type 'YES'")
                    print(">> ")
                    val answer = readln()
                    if(answer == "YES"){
                        if(service.removeFilmById(filmId)){
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
                "5" -> {
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