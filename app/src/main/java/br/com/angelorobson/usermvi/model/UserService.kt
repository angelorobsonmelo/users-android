package br.com.angelorobson.usermvi.model

import br.com.angelorobson.usermvi.model.dto.UserDto
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("users/")
    fun getUsers(): Observable<List<UserDto>>

    @GET("https://jsonplaceholder.typicode.com/users/{id}")
    fun getUserBy(@Path("id") id: Int): Single<UserDto>
}