package com.thkox.homeai.domain.usecase

import com.thkox.homeai.presentation.viewModel.welcome.EnterServerAddressState

class EnterServerAddressUseCase {
    fun execute(address: String): EnterServerAddressState {
        // Implement the server address validation logic here
        return EnterServerAddressState.Success
    }
}