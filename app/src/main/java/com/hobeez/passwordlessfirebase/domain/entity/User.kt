package com.hobeez.passwordlessfirebase.domain.entity

data class User(
    val birthday: String = "",
    val email: String = "",
    val name: String = "",
    val phone: String = ""
) {

    fun hasValidFields(): Boolean {
        return birthday != "" && name != ""
    }
}
