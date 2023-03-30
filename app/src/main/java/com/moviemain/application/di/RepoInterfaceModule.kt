package com.moviemain.application.di

import com.moviemain.data.RepositoryImpl
import com.moviemain.domain.RepositoryMovie
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoInterfaceModule {
    @Binds
    abstract fun datasource(impl: RepositoryImpl): RepositoryMovie
}