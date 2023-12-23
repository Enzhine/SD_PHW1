package ru.enzhine.phw

import ru.enzhine.phw.auth.dao.UserProfileDaoImpl
import ru.enzhine.phw.auth.service.UserProfileServiceImpl
import ru.enzhine.phw.cinema.dao.FilmDaoImpl
import ru.enzhine.phw.cinema.dao.FilmSessionDaoImpl
import ru.enzhine.phw.cinema.service.FilmServiceImpl
import ru.enzhine.phw.cinema.service.FilmSessionServiceImpl
import ru.enzhine.phw.cui.FilmSessionWidget
import ru.enzhine.phw.cui.base.ActingWidget
import ru.enzhine.phw.cui.base.BranchWidget
import ru.enzhine.phw.cui.FilmWidget
import ru.enzhine.phw.cui.TicketsWidget
import java.io.FileNotFoundException

fun main() {
    // using implementations
    val filmDao = FilmDaoImpl()
    val filmSessionDao = FilmSessionDaoImpl()
    val filmService = FilmServiceImpl(filmDao, filmSessionDao)
    val filmSessionService = FilmSessionServiceImpl(filmSessionDao)
    // widgets
    val exitWidget = object: ActingWidget {
        override val openTitle: String
            get() = "Exit"

        override fun run(): Boolean = false
    }
    val buyTicketWidget = TicketsWidget(filmSessionDao, filmSessionService)
    val controlFSessionsWidget = object: ActingWidget {
        override val openTitle: String
            get() = "Manage films-sessions (${filmSessionDao.listExisting().size})"

        override fun run(): Boolean {
            while(true){
                if(filmDao.listExisting().isEmpty()){
                    println(" FAILURE: there are no films to have related film-sessions!")
                    Thread.sleep(1000)
                    return true
                }
                val sessions = filmSessionDao.listExisting()
                println("[Managing film-sessions]")
                println(" Existing film-sessions:")
                if(sessions.isEmpty()){
                    println(" -there is no film-sessions!")
                }else{
                    for(ses in sessions){
                        println(" #S${ses.id} {start=${ses.start};filmTitle=${ses.film.title};...")
                    }
                }
                println(" Available options:")
                if(sessions.isNotEmpty()){
                    println(" #S? Edit <?> film-session")
                }
                println(" #0 Add new film-session")
                println(" #1 Back")
                print(">> ")
                val input = readln()
                if(sessions.isNotEmpty() && input.startsWith("S")){
                    try{
                        val id = input.substring(1).toLong()
                        filmSessionDao.findById(id) // exception catch
                        val fsw = FilmSessionWidget(id, filmSessionDao, filmSessionService)
                        fsw.run()
                    }catch (_: NumberFormatException){
                        println(" FAILED: Unexpected option!")
                        Thread.sleep(1000)
                    }catch (_: FileNotFoundException){
                        println(" FAILED: Wrong film-session id!")
                        Thread.sleep(1000)
                    }
                }else if(input == "0"){
                    val ses = FilmSessionWidget.createNewFilmSession(filmDao, filmSessionDao ,filmSessionService)
                    println(" SUCCESS: Created new film-session (id=${ses.id})")
                    Thread.sleep(1000)
                }else if(input == "1"){
                    return true
                }else{
                    println(" FAILED: Wrong action!")
                    Thread.sleep(1000)
                }
            }
        }
    }
    val controlFilmsWidget = object: ActingWidget {
        override val openTitle: String
            get() = "Manage films (${filmDao.listExisting().size})"

        override fun run(): Boolean {
            while(true){
                println("[Managing films]")
                println(" Existing films:")
                val films = filmDao.listExisting()
                if(films.isEmpty()){
                    println(" -there is no films!")
                }else{
                    for(film in films){
                        println(" #F${film.id} {title=${film.title};cost=${film.cost};...}")
                    }
                }
                println(" Available options:")
                if(films.isNotEmpty()){
                    println(" #F? Edit <?> film")
                }
                println(" #0 Add new film")
                println(" #1 Back")
                print(">> ")
                val input = readln()
                if(films.isNotEmpty() && input.startsWith("F")){
                    try{
                        val id = input.substring(1).toLong()
                        filmDao.findById(id) // exception catch
                        val fw = FilmWidget(id, filmDao, filmService, filmSessionDao)
                        fw.run()
                    }catch (_: NumberFormatException){
                        println(" FAILED: Unexpected option!")
                        Thread.sleep(1000)
                    }catch (_: FileNotFoundException){
                        println(" FAILED: Wrong film id!")
                        Thread.sleep(1000)
                    }
                }else if(input == "0"){
                    val film = FilmWidget.createNewFilm(filmService)
                    println(" SUCCESS: Created new film (id=${film.id})")
                    Thread.sleep(1000)
                }else if(input == "1"){
                    return true
                }else{
                    println(" FAILED: Wrong action!")
                    Thread.sleep(1000)
                }
            }
        }

    }
    val mainBw = BranchWidget(mutableListOf(buyTicketWidget, controlFilmsWidget, controlFSessionsWidget, exitWidget))
    // auth widgets
    val userService = UserProfileServiceImpl(UserProfileDaoImpl())
    val regWidget = object: ActingWidget {
        override val openTitle: String
            get() = "Sign-up"

        override fun run(): Boolean {
            while(true){
                println("[You are trying to signup (create a new user)]")
                println(" Please, input new login")
                print(">> ")
                val login = readln()
                println(" Please, input password")
                print(">> ")
                val password = readln()
                if(login.isEmpty() || password.isEmpty()){
                    println(" FAILED: Login or password can not be empty!")
                    Thread.sleep(1000)
                    continue
                }
                if(userService.register(login, password)){
                    println(" SUCCESS: New user successfully created!")
                    Thread.sleep(1000)
                    return true
                }else{
                    println(" FAILED: System error or such login already exists!")
                    Thread.sleep(1000)
                }
            }
        }
    }
    val logWidget = object: ActingWidget {
        override val openTitle: String
            get() = "Sign-in"

        override fun run(): Boolean {
            while(true){
                println("[You are trying to sign-in (login)]")
                println(" Existing login")
                print(">> ")
                val login = readln()
                println(" Password")
                print(">> ")
                val password = readln()
                if(userService.authorize(login, password)){
                    println(" SUCCESS: Welcome aboard captain ($login)")
                    Thread.sleep(1000)
                    while(mainBw.run()) {} // main widget execution loop
                    return false
                }else{
                    println(" FAILED: Such user not exists or wrong login/password!")
                    Thread.sleep(1000)
                }
            }
        }
    }
    val auth = BranchWidget(mutableListOf(logWidget, regWidget))
    while (auth.run()){} // auth widget execution loop
    println("See you later...")
}