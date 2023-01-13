package com.rorono.applicationcardbin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rorono.applicationcardbin.models.localmodels.CardBINItem
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

    private val _cardBINList = MutableLiveData<ArrayList<String>>()
    val cardBINList: LiveData<ArrayList<String>>
        get() = _cardBINList

    fun getCardInfo(bin: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                state.postValue(InfoCardState.Loading)
                val response = repository.getCardInfo(bin)
                withContext(Dispatchers.Main) {
                    when (response) {
                        is Result.Error -> state.postValue(
                            InfoCardState.Error("Ошибка! Не удалось получить данные. Проверьте Интернет соединение. Проверьте правильность введенных данных или попробуйте ввести более 4х символов")
                        )
                        is Result.Success -> {
                            _cardInfo.value = response.cardInfo
                            state.postValue(response.cardInfo?.let { InfoCardState.Success(it) })
                            val cardBINItem = CardBINItem(bin)
                            dataBase.insertCardBIN(cardBINItem = cardBINItem)
                            getCardBINListDataBase()
                        }
                    }
                }
            }
        }
    }


    fun getCardBINListDataBase(): ArrayList<String> {
        val listCardBIN = arrayListOf<String>()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = dataBase.getAllCardBIN()
                for (i in list) {
                    listCardBIN.add(i.cardBIN)
                }
                _cardBINList.postValue(listCardBIN)
            }
        }
        return listCardBIN
    }
}