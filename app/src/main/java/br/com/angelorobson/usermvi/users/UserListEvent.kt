package br.com.angelorobson.usermvi.users

import br.com.angelorobson.usermvi.model.User

sealed class UserListEvent

object Initial : UserListEvent()

data class UserLoaded(val users: List<User>) : UserListEvent()

data class UserClicked(val id: Int) : UserListEvent()
