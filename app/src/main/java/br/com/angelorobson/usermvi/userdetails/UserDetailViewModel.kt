package br.com.angelorobson.usermvi.userdetails

import br.com.angelorobson.usermvi.MobiusVM
import br.com.angelorobson.usermvi.model.UserRepository
import br.com.angelorobson.usermvi.utils.IdlingResource
import com.spotify.mobius.Next
import com.spotify.mobius.Next.next
import com.spotify.mobius.Update
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject


fun userDetailUpdate(
    model: UserDetailModel,
    event: UserDetailEvent
): Next<UserDetailModel, UserDetailEffect> {
    return when (event) {
        is Initial ->
            if (isOnline()) {
                next<UserDetailModel, UserDetailEffect>(
                    model.copy(isLoading = true),
                    setOf(LoadUser(event.id))
                )
            } else {
                next<UserDetailModel, UserDetailEffect>(
                    model.copy(isLoading = true),
                    setOf(LoadUserLocally(event.id))
                )
            }
        is UserLoaded -> next(
            model.copy(isLoading = false, user = event.user)
        )
        ErrorLoadingUSer -> next(model.copy(error = true, isLoading = false))
    }
}

class UserDetailViewModel @Inject constructor(
    userRepository: UserRepository,
    idlingResource: IdlingResource
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
                    .map { user ->
                        idlingResource.decrement()
                        UserLoaded(user = user) as UserDetailEvent
                    }
                    .onErrorReturn {
                        ErrorLoadingUSer
                    }

            }

        }
        .addTransformer(LoadUserLocally::class.java) { upstream ->
            upstream.switchMap { effect ->
                userRepository.getFromDatabase(effect.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .map { user ->
                        UserLoaded(user = user) as UserDetailEvent
                    }
            }
        }
        .build()

)

// ICMP
fun isOnline(): Boolean {
    val runtime = Runtime.getRuntime()
    try {
        val ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8")
        val exitValue = ipProcess.waitFor()
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }
    return false
}