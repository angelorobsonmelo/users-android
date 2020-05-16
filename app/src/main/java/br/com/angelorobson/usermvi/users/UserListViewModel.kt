package br.com.angelorobson.usermvi.users

import br.com.angelorobson.usermvi.MobiusVM
import br.com.angelorobson.usermvi.model.UserRepository
import br.com.angelorobson.usermvi.utils.Navigator
import com.spotify.mobius.Next
import com.spotify.mobius.Next.*
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

fun userListUpdate(
    model: UserListModel,
    event: UserListEvent
): Next<UserListModel, UserListEffect> {
    return when (event) {
        Initial -> dispatch(setOf(ObserverUsers))
        is UserLoaded -> next(
            model.copy(
                usersResult = UsersResult.UserLoaded(
                    event.users,
                    isLoading = false
                )
            )
        )
        is UserClicked -> dispatch(setOf(NavigateToUserDetail(event.id)))
    }
}


class UserListViewModel @Inject constructor(
    userRepository: UserRepository,
    navigator: Navigator
) : MobiusVM<UserListModel, UserListEvent, UserListEffect>(
    "UserViewModel",
    Update(::userListUpdate),
    UserListModel(),
    RxMobius.subtypeEffectHandler<UserListEffect, UserListEvent>()
        .addTransformer(ObserverUsers::class.java) { upstream ->
            upstream.switchMap {
                userRepository.getUsersFromApi()
                    .compose(applyObservableAsync())
                    .map {
                        UserLoaded(it)
                    }
            }
        }
        .addConsumer(NavigateToUserDetail::class.java) { effect ->
           print(effect)
        }
        .build()
)

fun <T> applyObservableAsync(): ObservableTransformer<T, T> {
    return ObservableTransformer { observable ->
        observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}