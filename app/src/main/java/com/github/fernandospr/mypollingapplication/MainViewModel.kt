package com.github.fernandospr.mypollingapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val repository: PersonRepository) : ViewModel() {

    private val _state = MutableLiveData<MainState>()
    val state: LiveData<MainState> = _state

    fun postPerson(person: Person) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            try {
                val result = repository.postPerson(person)
                    .withPolling { code -> repository.getPostPersonStatus(code) }
                _state.value = when (result) {
                    is Result.Approved -> MainState.Approved(result.payload!!)
                    is Result.Rejected -> MainState.Rejected
                    is Result.Pending -> MainState.Pending
                }
            } catch (e: Exception) {
                _state.value = MainState.Error
            }
        }
    }

    class Factory(private val repository: PersonRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}