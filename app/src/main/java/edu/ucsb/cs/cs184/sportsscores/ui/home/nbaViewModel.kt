package edu.ucsb.cs.cs184.sportsscores.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.ucsb.cs.cs184.sportsscores.Game

class nbaViewModel : ViewModel() {
    private val appStarted = MutableLiveData<Boolean>().apply {
        value = false
    }

    fun hasStarted(): Boolean? {
        return appStarted.value
    }

    fun changeAppStartedValue() {
        appStarted.value = !appStarted.value!!
    }

    val games = MutableLiveData<ArrayList<Game>>().apply {
        value = ArrayList()
    }

    fun getGames(): ArrayList<Game>? {
        return games.value
    }

    fun addGame(game: Game) {
        games.value?.add(game)
    }

    fun clearGames() {
        games.value?.clear()
    }

}