package br.com.angelorobson.usermvi.model

import br.com.angelorobson.usermvi.model.database.UserDao
import br.com.angelorobson.usermvi.model.dto.UserDto
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val service: UserService,
    private val userDao: UserDao
) {

    fun insert(users: List<User>): Completable {
        val usersDto = users.map {
            mapUserDto(it)
        }
        return userDao.insert(usersDto)
    }

    fun deleteAllFromDatabase(): Completable {
        return userDao.deleteAll()
    }

    fun getUsersFromApi(): Observable<List<User>> {
        return service.getUsers().map {
            it.map { userDto -> mapUser(userDto) }
        }
    }

    fun getUserFromApiBy(id: Int): Single<User> {
        return service.getUserBy(id).map {
            mapUser(it)
        }
    }

    fun getFromDatabase(id: Int): Single<User> {
        return userDao.getUser(id)
            .map { mapUser(it) }
    }
}

fun mapUser(dto: UserDto): User {
    return User(
        id = dto.id,
        name = dto.name,
        username = dto.username
    )
}

fun mapUserDto(user: User): UserDto {
    return UserDto(
        id = user.id,
        name = user.name,
        username = user.username
    )
}
