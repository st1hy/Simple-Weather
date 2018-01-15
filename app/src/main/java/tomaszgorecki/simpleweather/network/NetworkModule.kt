package tomaszgorecki.simpleweather.network

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.createWithScheduler
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class Accuweather

@Module
abstract class NetworkModule {

    @Module
    companion object {
        @Singleton
        @Provides
        @Accuweather
        @JvmStatic
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                    .baseUrl("http://dataservice.accuweather.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(createWithScheduler(Schedulers.io()))
                    .build()
        }
    }



}