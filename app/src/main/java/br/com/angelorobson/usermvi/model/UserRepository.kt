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

    fun insert(userDto: UserDto): Completable {
        return userDao.insert(userDto)
    }

    fun getFromDatabase(id: Int): Single<User> {
        return userDao.getUser(id)
            .map { mapUser(it) }
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
}

fun mapUser(dto: UserDto): User {
    return User(
        id = dto.id,
        name = dto.name,
        username = dto.username
    )
}
