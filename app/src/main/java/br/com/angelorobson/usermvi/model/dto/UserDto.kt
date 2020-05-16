package br.com.angelorobson.usermvi.model.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    val id: Int,
    val name: String,
    val username: String
)