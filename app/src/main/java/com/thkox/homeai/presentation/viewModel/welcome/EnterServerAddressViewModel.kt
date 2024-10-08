package com.thkox.homeai.presentation.viewModel.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thkox.homeai.domain.usecase.EnterServerAddressUseCase
import com.thkox.homeai.domain.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnterServerAddressViewModel @Inject constructor(
    private val enterServerAddressUseCase: EnterServerAddressUseCase
) : ViewModel() {

    private val _serverAddress = MutableLiveData<String>()
    val serverAddress: LiveData<String> = _serverAddress

    private val _enterServerAddressState = MutableLiveData<Result<Unit>>()
    val enterServerAddressState: LiveData<Result<Unit>> = _enterServerAddressState

    fun onServerAddressChanged(newAddress: String) {
        _serverAddress.value = newAddress
    }

    fun validateServerAddress() {
        val currentAddress = _serverAddress.value ?: ""

        viewModelScope.launch {
            _enterServerAddressState.value = Result.Loading()
            val result = enterServerAddressUseCase.invoke(currentAddress)
            _enterServerAddressState.value = result
        }
    }
}