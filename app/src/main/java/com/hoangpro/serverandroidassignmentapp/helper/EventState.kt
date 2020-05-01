package com.hoangpro.serverandroidassignmentapp.helper

enum class EventState {
    EVENT_LOGOUT,
    CHANGE_USER_INFORMATION,
    CHANGE_TOTAL_MONEY,
    ADD_CART,
    ADD_BILL
}

data class MessageEvent(val eventState: EventState,val data:Any)