package br.com.angelorobson.usermvi.di

import android.content.Context
import br.com.angelorobson.usermvi.model.database.UserDao
import br.com.angelorobson.usermvi.utils.IdlingResource
import br.com.angelorobson.usermvi.utils.TestIdlingResource
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class, ApiModule::class, TestModule::class])
interface TestComponent : ApplicationComponent {

    fun idlingResource(): IdlingResource

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun userDao(userDao: UserDao): Builder

        fun build(): TestComponent
    }
}

@Module
object TestModule {


    @Provides
    @Singleton
    @JvmStatic
    fun idlingResource(): IdlingResource = TestIdlingResource()
}