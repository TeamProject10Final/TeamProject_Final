package com.nomorelateness.donotlate.core.data.di

import com.nomorelateness.donotlate.core.data.session.SessionManagerImpl
import com.nomorelateness.donotlate.core.domain.session.SessionManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionModule {

    @Binds
    @Singleton
    abstract fun bindSessionManager(
        sessionManagerImpl: SessionManagerImpl
    ): SessionManager
}