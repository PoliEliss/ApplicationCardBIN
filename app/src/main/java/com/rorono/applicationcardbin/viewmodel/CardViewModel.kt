package com.rorono.applicationcardbin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rorono.applicationcardbin.models.removemodels.CardInfo
import com.rorono.applicationcardbin.repository.NetworkRepository
import com.rorono.applicationcardbin.repository.RepositoryDataBase
import com.rorono.applicationcardbin.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardViewModel(
    private val repository: NetworkRepository,
    private val dataBase: RepositoryDataBase
) : BaseViewModel<InfoCardState>() {

    private val _cardInfo = MutableLiveData<CardInfo>()
    val cardInfo: LiveData<CardInfo>
        get() = _cardInfo

    fun getCardInfo(bin: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                state.postValue(InfoCardState.Loading)
                val response = repository.getCardInfo(bin)
                withContext(Dispatchers.Main) {
                    when (response) {
                        is Result.Error -> state.postValue(
                            InfoCardState.Error("Ошибка! Не удалось получить данные. Проверьте правильность введенных данных или попробуйте ввести более 4х символов")
                        )
                        is Result.Success -> {
                            _cardInfo.value = response.cardInfo
                            state.postValue(response.cardInfo?.let { InfoCardState.Success(it) })
                        }
                    }
                }
            }
        }
    }


}