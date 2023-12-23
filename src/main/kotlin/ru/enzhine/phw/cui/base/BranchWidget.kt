package ru.enzhine.phw.cui.base

import java.lang.NumberFormatException

open class BranchWidget(private val actions: MutableList<ActingWidget>): ActingWidget {
    override val openTitle: String
        get() = "Branch widget of ${actions.size} options"

    override fun run(): Boolean {
        while(true){
            println("[Please, choose option]:")
            for((i, action) in actions.withIndex()){
                println(" #$i ${action.openTitle}")
            }
            print(">> ")
            val inp = readln()
            try{
                val num = inp.toInt()
                if(num !in 0 until actions.size){
                    println("Wrong input!")
                    Thread.sleep(1000)
                }else{
                    return actions[num].run()
                }
            }catch (ex: NumberFormatException){
                println("Wrong input!")
                Thread.sleep(1000)
            }
        }
    }
}