package br.com.angelorobson.usermvi.users

sealed class UserListEffect

object ObserverUsers : UserListEffect()

data class NavigateToUserDetail(val id: Int) : UserListEffect()
