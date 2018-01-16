package tomaszgorecki.simpleweather.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AccuweatherService {

    @GET("/locations/v1/cities/autocomplete")
    fun autompleteCity(@Query("apikey") apikey: String = API_KEY,
                       @Query("q") query: String,
                       @Query("language") languageCode: String): Single<Collection<CityCandidates>>

    companion object {
        const val API_KEY = "nJ4i62GXQrkzlBBegafucnO5RyACnIZO"
    }
}