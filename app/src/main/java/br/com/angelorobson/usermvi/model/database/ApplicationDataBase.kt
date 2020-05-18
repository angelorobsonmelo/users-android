package br.com.angelorobson.usermvi.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.angelorobson.usermvi.model.dto.UserDto

@Database(
    entities = [UserDto::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
}