package tomaszgorecki.simpleweather.network

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import paperparcel.PaperParcel
import paperparcel.PaperParcelable
import retrofit2.http.GET
import retrofit2.http.Query

interface AccuweatherService {

    @GET("locations/v1/cities/autocomplete")
    fun autompleteCity(@Query("apikey") apikey: String = API_KEY,
                       @Query("q") query: String,
                       @Query("language") languageCode: String): Single<Collection<AccuCityCandidates>>

    companion object {
        const val API_KEY = "nJ4i62GXQrkzlBBegafucnO5RyACnIZO"
        const val BASE_URI = "http://dataservice.accuweather.com/"
    }
}

@PaperParcel
data class AccuCityCandidates(var Version: Int,
                              var Key: String,
                              var Type: String,
                              var Rank: Int,
                              var LocalizedName: String,
                              @SerializedName("Country.ID") var CountryID: String,
                              @SerializedName("Country.LocalizedName") var CountryLocalizedName: String,
                              @SerializedName("AdministrativeArea.ID") var AdministrativeAreaID: String,
                              @SerializedName(" AdministrativeArea.LocalizedName ") var AdministrativeAreaLocalizedName: String) : SearchSuggestion, PaperParcelable {
    override fun getBody(): String {
        return LocalizedName
    }

    companion object {
        @JvmField val CREATOR = PaperParcelAccuCityCandidates.CREATOR
    }
}