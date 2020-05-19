package br.com.angelorobson.usermvi.usersdetails

import br.com.angelorobson.usermvi.model.User
import br.com.angelorobson.usermvi.userdetails.*
import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import io.mockk.every
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


class UserDetailsUpdateTest {

    private lateinit var updateSpec: UpdateSpec<UserDetailModel, UserDetailEvent, UserDetailEffect>


    @Before
    fun setUp() {
        updateSpec = UpdateSpec(::userDetailUpdate)
    }

    @Test
    fun InitialEvent_DispatchLoadUser() {
        mockkStatic("br.com.angelorobson.usermvi.userdetails.UserDetailViewModelKt")
        every { isOnline() } returns true

        val model = UserDetailModel(isLoading = true)
        val id = 1

        updateSpec
            .given(model)
            .whenEvent(Initial(id))
            .then(
                assertThatNext<UserDetailModel, UserDetailEffect>(
                    hasModel(model.copy(isLoading = true)),
                    hasEffects(LoadUser(id))
                )
            )
    }

    @Test
    fun InitialEvent_DispatchLoadUserLocally() {
        mockkStatic("br.com.angelorobson.usermvi.userdetails.UserDetailViewModelKt")
        every { isOnline() } returns false

        val model = UserDetailModel(isLoading = true)
        val id = 1

        updateSpec
            .given(model)
            .whenEvent(Initial(id))
            .then(
                assertThatNext<UserDetailModel, UserDetailEffect>(
                    hasModel(model.copy(isLoading = true)),
                    hasEffects(
                        LoadUserLocally(id)
                    )
                )
            )
    }

    @Test
    fun LoadUserEvent_HasModelWithUSer() {
        val user = User(1, "name", "username")
        val model = UserDetailModel(user = user)

        updateSpec
            .given(model)
            .whenEvent(UserLoaded(user))
            .then(
                assertThatNext<UserDetailModel, UserDetailEffect>(
                    hasModel(model.copy(isLoading = false, user = user)),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun ErrorLoadingUSer_HasModelWithError() {
        val model = UserDetailModel()

        updateSpec
            .given(model)
            .whenEvent(ErrorLoadingUSer)
            .then(
                assertThatNext<UserDetailModel, UserDetailEffect>(
                    hasModel(model.copy(error = true, isLoading = false)),
                    hasNoEffects()
                )
            )
    }

}