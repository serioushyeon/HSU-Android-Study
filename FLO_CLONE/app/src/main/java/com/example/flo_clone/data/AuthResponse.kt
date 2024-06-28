package com.example.flo_clone.data

data class AuthResponse (
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
)