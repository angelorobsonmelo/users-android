package br.com.angelorobson.usermvi

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import br.com.angelorobson.usermvi.di.ApplicationComponent
import br.com.angelorobson.usermvi.di.DaggerRealComponent
import kotlin.reflect.KClass

class App : Application() {

    open val component: ApplicationComponent by lazy {
        DaggerRealComponent.builder()
            .context(this)
            .build()
    }

}

val Context.component: ApplicationComponent
    get() = (this.applicationContext as App).component

fun <T, M, E> Fragment.getViewModel(type: KClass<T>): BaseViewModel<M, E> where T : ViewModel, T : BaseViewModel<M, E> {
    val factory = this.context!!.component.viewModelFactory()
    return ViewModelProviders.of(this, factory)[type.java]
}

fun <T, M, E> FragmentActivity.getViewModel(type: KClass<T>): BaseViewModel<M, E> where T : ViewModel, T : BaseViewModel<M, E> {
    val factory = this.applicationContext!!.component.viewModelFactory()
    return ViewModelProviders.of(this, factory)[type.java]
}
