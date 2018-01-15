package tomaszgorecki.simpleweather.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AccuweatherService {

    @GET("/locations/v1/cities/autocomplete?apikey=" + API_KEY)
    fun autompleteCity(@Query("q") name: String,
                       @Query("language") languageCode: String): Single<AutoCompleteResponse>

    companion object {
        const val API_KEY = "nJ4i62GXQrkzlBBegafucnO5RyACnIZO"
    }
}