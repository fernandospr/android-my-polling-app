package com.github.fernandospr.mypollingapplication

sealed class MainState {
    object Loading: MainState()
    object Error: MainState()

    data class Approved(val person: Person): MainState()
    object Pending: MainState()
    object Rejected: MainState()
}