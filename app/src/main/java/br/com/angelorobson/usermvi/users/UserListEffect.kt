package br.com.angelorobson.usermvi.users

import br.com.angelorobson.usermvi.model.User

sealed class UserListEffect

object ObserverUsers : UserListEffect()

data class SaveUsersLocally(val users: List<User>) : UserListEffect()

data class NavigateToUserDetail(val id: Int) : UserListEffect()
