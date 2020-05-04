package com.lab5.pixabayapp.di

import android.app.Application
import com.lab5.pixabayapp.PixBayApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    AppModule::class,
    MainActivityModule::class])
interface AppComponent {

    // Fields injection to inject dispatchingAndroidInjector in the BixBayApp
    fun inject (pixBayApp: PixBayApp)

    @Component.Builder
    interface Builder {

        //  To pass an instance of Application at Runtime
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}