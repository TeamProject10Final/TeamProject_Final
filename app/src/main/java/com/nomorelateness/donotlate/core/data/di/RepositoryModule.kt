package com.nomorelateness.donotlate.core.data.di

import com.google.android.datatransport.runtime.dagger.Module
import com.nomorelateness.donotlate.core.data.repository.FirebaseDataRepositoryImpl
import com.nomorelateness.donotlate.core.data.repository.PromiseRoomRepositoryImpl
import com.nomorelateness.donotlate.core.data.repository.UserRepositoryImpl
import com.nomorelateness.donotlate.core.domain.repository.FirebaseDataRepository
import com.nomorelateness.donotlate.core.domain.repository.PromiseRoomRepository
import com.nomorelateness.donotlate.core.domain.repository.UserRepository
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // application level
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFirebaseDataRepository(
        firebaseDataRepositoryImpl: FirebaseDataRepositoryImpl
    ): FirebaseDataRepository

    @Binds
    @Singleton
    abstract fun bindPromiseRoomRepository(
        promiseRoomRepositoryImpl: PromiseRoomRepositoryImpl
    ): PromiseRoomRepository

    @Binds
    @Singleton
    abstract fun bindFirebaseDataRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

}