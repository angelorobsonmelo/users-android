package br.com.angelorobson.usermvi.users

import br.com.angelorobson.usermvi.model.User

data class UserListModel(
    val usersResult: UsersResult = UsersResult.UserLoaded()
)

sealed class UsersResult {
    object Empty : UsersResult()
    object Error : UsersResult()
    data class UserLoaded(val users: List<User> = listOf(), val isLoading: Boolean = true) :
        UsersResult()
}