package ru.enzhine.phw.cui.base

interface ActingWidget {
    /**
     * @return widget title (its description at options menu)
     */
    val openTitle: String

    /**
     * @return repeat previous action
     */
    fun run(): Boolean
}