package br.com.angelorobson.usermvi.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.angelorobson.usermvi.model.dto.UserDto
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class UserDao {

    @Query("SELECT * FROM UserDto WHERE id=:id")
    abstract fun getUser(id: Int): Single<UserDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(photos: List<UserDto>): Completable

    @Query("DELETE FROM UserDto")
    abstract fun deleteAll(): Completable

}