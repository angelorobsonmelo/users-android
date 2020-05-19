package br.com.angelorobson.usermvi.users

import br.com.angelorobson.usermvi.MobiusVM
import br.com.angelorobson.usermvi.model.UserRepository
import br.com.angelorobson.usermvi.utils.IdlingResource
import br.com.angelorobson.usermvi.utils.Navigator
import com.spotify.mobius.Next
import com.spotify.mobius.Next.dispatch
import com.spotify.mobius.Next.next
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
            ),
            setOf(SaveUsersLocally(event.users))
        )
        is UserClicked -> dispatch(setOf(NavigateToUserDetail(event.id)))
        ErrorOccurred -> next(
            model.copy(usersResult = UsersResult.Error)
        )
        UserListEmpty -> next(
            model.copy(usersResult = UsersResult.Empty)
        )
    }
}


class UserListViewModel @Inject constructor(
    userRepository: UserRepository,
    navigator: Navigator,
    idlingResource: IdlingResource
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
                        idlingResource.decrement()
                        if (it.isEmpty()) {
                            UserListEmpty as UserListEvent
                        }

                        UserLoaded(it) as UserListEvent
                    }
                    .onErrorReturn {
                        ErrorOccurred
                    }
            }
        }
        .addTransformer(SaveUsersLocally::class.java) { upstream ->
            upstream.switchMap { event ->
                userRepository.insert(event.users)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable<UserListEvent>()
            }
        }
        .addConsumer(NavigateToUserDetail::class.java) { effect ->
            navigator.to(UserListFragmentDirections.userDetail(effect.id.toLong()))
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