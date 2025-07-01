package com.example.starlinkloginapp.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.starlinkloginapp.Conexion.AuthRepository
import com.example.starlinkloginapp.Conexion.Datos.Instance
import com.example.starlinkloginapp.Conexion.Models.LoginRequest
import com.example.starlinkloginapp.Conexion.Models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel(){
    private val repo = AuthRepository()

    private val _correoUsuario = MutableStateFlow<String?>(null)
    val correoUsuario: StateFlow<String?> = _correoUsuario

    private val  _status = MutableStateFlow<String?>(null)
    val status: StateFlow<String?> = _status

    fun login(correo: String, password: String) = viewModelScope.launch { 
        val result = repo.login(LoginRequest(correo, password))
        result
            .onSuccess { 
                Instance.token = it.token
                _correoUsuario.value = correo
                _status.value = "OK"
            }
            .onFailure { e -> 
                _status.value = e.message
            }
    }

    fun signup(
        nombre: String,
        apellido: String,
        correo: String,
        contraseña: String) {

        viewModelScope.launch {
            val result = repo.signup(nombre, apellido, correo, contraseña)
            _status.value = result
        }
    }

    fun clearStatus(){
        _status.value = null
    }
}