package com.rorono.applicationcardbin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.rorono.applicationcardbin.repository.NetworkRepository
import com.rorono.applicationcardbin.repository.RepositoryDataBase

class CardViewModelFactory(
    private val networkRepository: NetworkRepository,
    private val dataBase: RepositoryDataBase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return CardViewModel(networkRepository, dataBase) as T
    }
}