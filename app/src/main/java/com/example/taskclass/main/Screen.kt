package com.example.taskclass.main

enum class Screen(val route: String) {
    MAIN("MAIN"),
    AGENDA("AGENDA"),
    EVENTS("EVENTS"),
    NEW_EVENT("NEW_EVENT"),
    NOTES("NOTES"),
    DISCIPLINE("DISCIPLINE"),
    DISCIPLINE_CREATE("DISCIPLINE_CREATE"),
    DISCIPLINE_EDIT("DISCIPLINE_EDIT/{disciplineId}"),
    SCHEDULES("SCHEDULES"),
    NEW_SCHEDULES("NEW_SCHEDULES"),
    TYPE_EVENTS("TYPE_EVENTS");

    fun withArgs(vararg args: String): String {
        var newRoute = route
        args.forEach { arg ->
            newRoute = newRoute.replaceFirst("\\{.*?\\}".toRegex(), arg)
        }
        return newRoute
    }
}