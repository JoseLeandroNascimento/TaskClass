package com.example.taskclass.ui.main

enum class Screen(val route: String) {
    MAIN("MAIN"),
    AGENDA("AGENDA"),
    EVENTS("EVENTS"),
    EVENT_DETAIL("EVENT_DETAIL/{eventId}"), // 👈 nova rota
    NEW_EVENT("NEW_EVENT"),
    EVENT_ALL("EVENT_ALL"),
    EVENT_CREATE("EVENT_CREATE"),
    EVENT_EDIT("EVENT_EDIT/{eventId}"),
    NOTES("NOTES"),
    NOTE_CREATE("NOTE_CREATE"),
    NOTE_EDIT("NOTE_EDIT/{idNote}"),
    DISCIPLINE("DISCIPLINE"),
    DISCIPLINE_CREATE("DISCIPLINE_CREATE"),
    DISCIPLINE_EDIT("DISCIPLINE_EDIT/{disciplineId}"),
    SCHEDULES("SCHEDULES"),
    NEW_SCHEDULES("NEW_SCHEDULES"),
    EDIT_SCHEDULES("EDIT_SCHEDULES/{scheduleId}"),
    TYPE_EVENTS("TYPE_EVENTS"),
    TYPE_EVENT_CREATE("TYPE_EVENT_CREATE"),
    TYPE_EVENT_EDIT("TYPE_EVENT_EDIT/{typeId}"),
    BULLETINS("BULLETINS"),
    BULLETIN_CREATE("BULLETIN_CREATE");

    fun withArgs(vararg args: String): String {
        var newRoute = route
        args.forEach { arg ->
            newRoute = newRoute.replaceFirst("\\{.*?\\}".toRegex(), arg)
        }
        return newRoute
    }
}
