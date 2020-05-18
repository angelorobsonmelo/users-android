package br.com.angelorobson.usermvi.userdetails

import android.provider.SyncStateContract.Helpers.update
import br.com.angelorobson.usermvi.MobiusVM
import br.com.angelorobson.usermvi.model.UserRepository
import br.com.angelorobson.usermvi.users.applyObservableAsync
import com.spotify.mobius.Next
import com.spotify.mobius.Next.*
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.operators.single.SingleInternalHelper.toObservable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


fun userDetailUpdate(
    model: UserDetailModel,
    event: UserDetailEvent
): Next<UserDetailModel, UserDetailEffect> {
    return when (event) {
        is Initial -> next(
            model.copy(isLoading = true),
            setOf(LoadUser(event.id))
        )
        is UserLoaded -> next(
            model.copy(isLoading = false, user = event.user)
        )
        ErrorLoadingUSer -> next(model.copy(error = true, isLoading = false))
    }
}

class UserDetailViewModel @Inject constructor(
    userRepository: UserRepository
) : MobiusVM<UserDetailModel, UserDetailEvent, UserDetailEffect>(
    "UserDetailViewModel",
    Update(::userDetailUpdate),
    UserDetailModel(),
    RxMobius.subtypeEffectHandler<UserDetailEffect, UserDetailEvent>()
        .addTransformer(LoadUser::class.java) { upstream ->
            upstream.switchMap { effect ->
                userRepository.getUserFromApiBy(effect.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .retry(2)
                    .map { user ->
                        UserLoaded(user = user) as UserDetailEvent
                    }
                    .onErrorReturn {
                        ErrorLoadingUSer
                    }
            }

        }.build()

)