package br.com.angelorobson.usermvi.userdetails

sealed class UserDetailEffect

data class LoadUser(val id: Int) : UserDetailEffect()
data class LoadUserLocally(val id: Int) : UserDetailEffect()
