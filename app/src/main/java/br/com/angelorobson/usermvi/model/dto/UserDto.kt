package br.com.angelorobson.usermvi.model.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
data class UserDto(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String
)