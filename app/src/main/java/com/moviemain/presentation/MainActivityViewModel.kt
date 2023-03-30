package com.moviemain.presentation

import androidx.lifecycle.ViewModel
import com.moviemain.core.connectivity.ConnectivityNetwork
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(connectivityNetwork: ConnectivityNetwork) :
    ViewModel() {

    val isConnected: Flow<Boolean> = connectivityNetwork.isConnected

}