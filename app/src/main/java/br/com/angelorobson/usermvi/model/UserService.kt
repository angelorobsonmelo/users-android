package br.com.angelorobson.usermvi.model

import br.com.angelorobson.usermvi.model.dto.UserDto
import io.reactivex.Observable
import retrofit2.http.GET

interface UserService {

    @GET("users/")
    fun getUsers(): Observable<List<UserDto>>
}