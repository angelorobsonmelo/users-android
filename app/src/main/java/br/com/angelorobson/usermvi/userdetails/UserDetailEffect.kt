package br.com.angelorobson.usermvi.userdetails

sealed class UserDetailEffect

data class LoadUser(val id: Int) : UserDetailEffect()
