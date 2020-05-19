package br.com.angelorobson.usermvi.users

import br.com.angelorobson.usermvi.model.User
import com.spotify.mobius.test.NextMatchers.*
import com.spotify.mobius.test.UpdateSpec
import com.spotify.mobius.test.UpdateSpec.assertThatNext
import org.junit.Before
import org.junit.Test

class UserListUpdateTest {

    private lateinit var updateSpec: UpdateSpec<UserListModel, UserListEvent, UserListEffect>

    @Before
    fun before() {
        updateSpec = UpdateSpec(::userListUpdate)
    }

    @Test
    fun userInitialEvent_ObserverUsersDispatched() {
        val model = UserListModel()
        updateSpec
            .given(model)
            .whenEvent(Initial)
            .then(
                assertThatNext<UserListModel, UserListEffect>(
                    hasNoModel(),
                    hasEffects(ObserverUsers)
                )
            )
    }

    @Test
    fun userLoadedEvent_UserLoaded() {
        val model = UserListModel()
        val users = listOf(
            User(1, "name1", "username1"),
            User(2, "name2", "username2")
        )
        updateSpec
            .given(model)
            .whenEvent(UserLoaded(users))
            .then(
                assertThatNext<UserListModel, UserListEffect>(
                    hasModel(
                        model.copy(
                            usersResult = UsersResult.UserLoaded(users = users, isLoading = false)
                        )
                    ),
                    hasEffects(SaveUsersLocally(users))
                )
            )
    }

    @Test
    fun userClickedEvent_NavigateToUserDetailDispatched() {
        val model = UserListModel()
        val userId = 1
        updateSpec
            .given(model)
            .whenEvent(UserClicked(userId))
            .then(
                assertThatNext<UserListModel, UserListEffect>(
                    hasNoModel(),
                    hasEffects(NavigateToUserDetail(userId))
                )
            )
    }

    @Test
    fun errorOccurred_updateModelAndHasNoEffect() {
        val model = UserListModel()
        updateSpec
            .given(model)
            .whenEvent(ErrorOccurred)
            .then(
                assertThatNext<UserListModel, UserListEffect>(
                    hasModel(
                        model.copy(usersResult = UsersResult.Error)
                    ),
                    hasNoEffects()
                )
            )
    }

    @Test
    fun UserListEmpty_resultEmpty() {
        val model = UserListModel()
        updateSpec
            .given(model)
            .whenEvent(UserListEmpty)
            .then(
                assertThatNext<UserListModel, UserListEffect>(
                    hasModel(
                        model.copy(usersResult = UsersResult.Empty)
                    ),
                    hasNoEffects()
                )
            )
    }


}