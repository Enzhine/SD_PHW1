package ru.enzhine.phw.cinema.entities

import com.fasterxml.jackson.annotation.JsonProperty

class TheatreState(
    @JsonProperty("rows")
    val rows: Int,
    @JsonProperty("width")
    val width: Int
    ){
    enum class PlaceState{
        FREE,
        SOLD
    }
    @JsonProperty("places")
    val places: Array<Array<Pair<PlaceState, String>>> =
        Array(rows) {Array(width) { Pair(PlaceState.FREE, "") }}

    private fun validateOrException(row: Int, place: Int){
        if(row !in 0 until rows){
            throw IllegalArgumentException("Row must be in [0:$rows) interval!")
        }
        if(place !in 0 until width){
            throw IllegalArgumentException("Place must be in [0:$width) interval!")
        }
    }

    fun isPlaceSold(row: Int, place: Int): Boolean {
        validateOrException(row, place)
        return places[row][place].first == PlaceState.SOLD
    }

    fun placeFree(row: Int, place: Int) {
        validateOrException(row, place)
        places[row][place] = Pair(PlaceState.FREE, "")
    }

    fun verifyPlace(row: Int, place: Int, key: String): Boolean {
        validateOrException(row, place)
        return places[row][place].second == key
    }

    fun placeSale(row: Int, place: Int, key: String) {
        validateOrException(row, place)
        places[row][place] = Pair(PlaceState.SOLD, key)
    }
}