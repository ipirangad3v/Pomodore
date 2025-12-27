package digital.tonima.pomodore.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import digital.tonima.pomodore.data.repository.PomodoroRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePomodoroRepository(
        @ApplicationContext context: Context
    ): PomodoroRepository {
        return PomodoroRepository(context)
    }
}

