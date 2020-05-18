package br.com.angelorobson.usermvi.userdetails

import br.com.angelorobson.usermvi.model.User

data class UserDetailModel(
    val isLoading: Boolean = true,
    val user: User? = null,
    val error: Boolean = false
)