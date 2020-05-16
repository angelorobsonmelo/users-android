package br.com.angelorobson.usermvi.model

import br.com.angelorobson.usermvi.model.dto.UserDto
import io.reactivex.Observable
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService
) {

    fun getUsersFromApi(): Observable<List<User>> {
        return service.getUsers().map {
            it.map { userDto -> mapUser(userDto) }
        }
    }
}

fun mapUser(dto: UserDto): User {
    return User(
        id = dto.id,
        name = dto.name,
        username = dto.username
    )
}
