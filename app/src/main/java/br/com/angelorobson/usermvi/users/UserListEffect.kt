package br.com.angelorobson.usermvi.users

sealed class UserListEffect

object ObserverUsers : UserListEffect()

object NavigateScanner : UserListEffect()

data class NavigateToUserDetail(val id: Int) : UserListEffect()
