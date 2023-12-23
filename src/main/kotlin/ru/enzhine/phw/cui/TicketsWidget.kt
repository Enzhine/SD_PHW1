package ru.enzhine.phw.cui

import ru.enzhine.phw.cinema.dao.FilmSessionDao
import ru.enzhine.phw.cinema.entities.FilmSession
import ru.enzhine.phw.cinema.service.FilmSessionService
import ru.enzhine.phw.cui.base.ActingWidget
import java.lang.Exception
import java.time.LocalDateTime
import java.util.UUID

class TicketsWidget(private val filmSessionDao: FilmSessionDao, private val filmSessionService: FilmSessionService): ActingWidget {
    override val openTitle: String
        get() = "Manage tickets"

    override fun run(): Boolean {
        while (true){
            val sessions = filmSessionDao.listExisting()
            if(sessions.isEmpty()){
                println(" FAILURE: there are no film-sessions!")
                Thread.sleep(1000)
                return true
            }
            println("[Managing tickets]")
            println(" Future or going film-sessions:")
            for(ses in sessions){
                if(ses.start.plusSeconds(ses.film.length) >= LocalDateTime.now()){
                    println(" #${ses.id} {start=${ses.start};filmTitle=${ses.film.title};...")
                }
            }
            var fs: FilmSession?
            while(true){
                println(" Please, select film-session")
                val id = FilmWidget.readLong(">> ")
                fs = sessions.find { it.id == id }
                if(fs != null){
                    break
                }else{
                    println(" FAILED: such film session not exists!")
                }
            }
            while(true) {
                println("[Selected film-session ${fs!!.id}]")
                println(" | start=${fs.start}")
                println(" | end=${fs.start.plusSeconds(fs.film.length)}")
                println(" | costPerSeat=${fs.film.cost}")
                println(" | filmName=${fs.film.title}")
                println(" | filmLength=${fs.film.length}")
                println(filmSessionService.representFilmSession(fs))
                println(" Available options:")
                println(" #0 Sale ticket(s)")
                println(" #1 Register ticket(s)")
                println(" #2 Refund ticket(s)")
                println(" #3 Cancel")
                when(readln()){
                    "0" -> {
                        // SALE TICKETS
                        var amount: Int
                        var flag = false
                        while(true){
                            println(" Please, input amount of tickets")
                            amount = FilmWidget.readInt(">> ")
                            if(amount < 0){
                                println(" ERROR: amount can not be negative!")
                                Thread.sleep(1000)
                                continue
                            }else if(amount == 0){
                                println(" CANCELLED")
                                flag = true
                                Thread.sleep(1000)
                                break
                            }else{
                                break
                            }
                        }
                        if(flag){
                            continue
                        }
                        val rp: Array<Pair<Int, Int>> = Array(amount) {
                            println(" Ticket ${it+1}: Please, input row[0:${fs!!.theatreState.rows}) and place[0:${fs!!.theatreState.width}) (row place)")
                            var row: Int
                            var place: Int
                            while(true){
                                try {
                                    print(">> ")
                                    val read = readln().split(" ")
                                    row = read[0].toInt()
                                    place = read[1].toInt()
                                    if(fs!!.theatreState.isPlaceSold(row, place)){
                                        println(" ERROR: Such ticket already sold!")
                                        continue
                                    }
                                    break
                                }catch (_: Exception){
                                    println(" ERROR: Wrong input!")
                                }
                            }
                            Pair(row,place)
                        }
                        println(" Total cost ${fs.film.cost * amount}")
                        println(" Confirm $amount tickets purchase at [${rp.joinToString { it.first.toString().plus(" ").plus(it.second) }}] theatre positions, type 'YES'")
                        print(">> ")
                        if(readln() == "YES"){
                            for ((i, pair) in rp.withIndex()){
                                val key = UUID.randomUUID().toString()
                                if(filmSessionService.saleFilmSessionPlaceById(fs.id, pair.first, pair.second, key)){
                                    println("Ticket ${i+1} at $pair token=$key")
                                }else{
                                    println(" ERROR: Something went wrong during ticket sale process! Please, proceed immediate refund!")
                                }
                            }
                            fs = filmSessionDao.findById(fs.id)
                            println(" SUCCESS: Please, provide tokens to customers.")
                            Thread.sleep(4000)
                        }else {
                            println(" CANCELLED")
                            Thread.sleep(1000)
                        }
                    }
                    "1" -> {
                        // REGISTER TICKETS
                        var amount: Int
                        var flag = false
                        while(true){
                            println(" Please, input amount of tickets")
                            amount = FilmWidget.readInt(">> ")
                            if(amount < 0){
                                println(" ERROR: Amount can not be negative!")
                                Thread.sleep(1000)
                                continue
                            }else if(amount == 0){
                                println(" CANCELLED")
                                flag = true
                                Thread.sleep(1000)
                                break
                            }else{
                                break
                            }
                        }
                        if(flag){
                            continue
                        }
                        for (i in 1 .. amount){
                            println(" Ticket ${i}: Please, input row[0:${fs.theatreState.rows}) and place[0:${fs.theatreState.width}) and key (row place key)")
                            while(true){
                                try {
                                    print(">> ")
                                    val read = readln().split(" ")
                                    val row = read[0].toInt()
                                    val place = read[1].toInt()
                                    val key = read[2]
                                    if(!fs.theatreState.isPlaceSold(row, place)){
                                        println(" ERROR: Such ticket not sold!")
                                        continue
                                    }
                                    if(fs.isRegistered(row, place)){
                                        println(" ERROR: Such ticket already registered!")
                                        continue
                                    }
                                    if(!filmSessionService.registerOnFilmSessionById(fs.id, row, place, key)){
                                        println(" ERROR: Wrong ticket key! Please, try again later.")
                                    }else{
                                        println(" OK: Ticket registered!")
                                    }
                                    break
                                }catch (_: Exception){
                                    println(" ERROR: Wrong input!")
                                }
                            }
                        }
                        fs = filmSessionDao.findById(fs.id)
                        println(" REGISTRATION COMPLETED")
                        Thread.sleep(1000)
                    }
                    "2" -> {
                        // REFUND TICKETS
                        var amount: Int
                        var flag = false
                        while(true){
                            println(" Please, input amount of tickets")
                            amount = FilmWidget.readInt(">> ")
                            if(amount < 0){
                                println(" ERROR: Amount can not be negative!")
                                Thread.sleep(1000)
                                continue
                            }else if(amount == 0){
                                println(" CANCELLED")
                                flag = true
                                Thread.sleep(1000)
                                break
                            }else{
                                break
                            }
                        }
                        if(flag){
                            continue
                        }
                        for (i in 1 .. amount){
                            println(" Ticket ${i}: Please, input row[0:${fs.theatreState.rows}) and place[0:${fs.theatreState.width}) and key (row place key)")
                            while(true){
                                try {
                                    print(">> ")
                                    val read = readln().split(" ")
                                    val row = read[0].toInt()
                                    val place = read[1].toInt()
                                    val key = read[2]
                                    if(!fs.theatreState.isPlaceSold(row, place)){
                                        println(" ERROR: Such ticket not sold!")
                                        continue
                                    }
                                    if(!fs.isRegistered(row, place)){
                                        println(" ERROR: Such ticket already registered!")
                                        continue
                                    }
                                    if(fs.start <= LocalDateTime.now()) {
                                        println(" ERROR: Its too late. The film-session has started!")
                                        continue
                                    }
                                    if(!filmSessionService.freeFilmSessionPlaceById(fs.id, row, place, key)){
                                        println(" ERROR: Wrong ticket key! Please, try again later.")
                                    }else{
                                        println(" OK: Ticket refund completed!")
                                    }
                                    break
                                }catch (_: Exception){
                                    println(" ERROR: Wrong input!")
                                }
                            }
                        }
                        fs = filmSessionDao.findById(fs.id)
                        println(" REFUND COMPLETED")
                        Thread.sleep(1000)
                    }
                    "3" -> {
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

}