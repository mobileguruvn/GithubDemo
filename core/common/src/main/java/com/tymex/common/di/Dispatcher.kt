package com.tymex.common.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: GithubDispatcher)

enum class GithubDispatcher {
    IO,
    Default
}
