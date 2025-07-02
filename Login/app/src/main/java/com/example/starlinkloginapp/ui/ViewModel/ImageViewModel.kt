package com.example.starlinkloginapp.ui.ViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ImageViewModel: ViewModel() {

    private val _imagenProcesada =
        MutableStateFlow<List<String>>(emptyList())
    val imagenProcesada: StateFlow<List<String>> = _imagenProcesada

    fun agregarImg(base64:String){
        _imagenProcesada.value = _imagenProcesada.value + base64
    }

    fun limpiar(){
        _imagenProcesada.value = emptyList()
    }
}