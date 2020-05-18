package br.com.angelorobson.usermvi.userdetails

import br.com.angelorobson.usermvi.model.User

sealed class UserDetailEvent

data class Initial(val id: Int) : UserDetailEvent()

data class UserLoaded(val user: User) : UserDetailEvent()

object ErrorLoadingUSer : UserDetailEvent()
