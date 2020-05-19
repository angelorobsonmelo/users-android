package br.com.angelorobson.usermvi

import br.com.angelorobson.usermvi.di.DaggerTestComponent
import br.com.angelorobson.usermvi.model.database.UserDao
import br.com.angelorobson.usermvi.model.dto.UserDto
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject


class AndroidTestApplication : App() {

    val userDtoSubject = PublishSubject.create<UserDto>()
    private val userDao: UserDao = object : UserDao() {
        override fun getUser(id: Int): Single<UserDto> {
            return userDtoSubject.single(UserDto(id, "", ""))
        }

        override fun insert(photos: List<UserDto>): Completable {
            return Completable.complete()
        }

        override fun deleteAll(): Completable {
            return Completable.complete()
        }

    }

    override val component by lazy {
        DaggerTestComponent.builder()
            .context(this)
            .userDao(userDao)
            .build()
    }
}