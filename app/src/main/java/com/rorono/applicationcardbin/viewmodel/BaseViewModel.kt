package com.rorono.applicationcardbin.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel <T> : ViewModel() {
    protected val state = MutableLiveData<T>()

    fun observeState(owner: LifecycleOwner, onChange: (newState: T) -> Unit) {
        state.observe(owner) {
            it.let(onChange)
        }
    }
}