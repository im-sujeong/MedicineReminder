package com.sujeong.pillo.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<BE: BaseUiEffect>: ViewModel() {
    private val _uiEffect: Channel<BE> = Channel()
    val uiEffect: Flow<BE> = _uiEffect.receiveAsFlow()

    open fun onUiEffect(uiEffect: BE): Job = viewModelScope.launch {
        _uiEffect.send(uiEffect)
    }
}