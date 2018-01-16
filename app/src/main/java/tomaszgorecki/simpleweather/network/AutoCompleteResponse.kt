package tomaszgorecki.simpleweather.network

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.google.gson.annotations.SerializedName
import tomaszgorecki.simpleweather.model.StringSearchSuggestion

data class CityCandidates(var Version: Int,
                          var Key: String,
                          var Type: String,
                          var Rank: Int,
                          var LocalizedName: String,
                          @SerializedName("Country.ID") var CountryID: String,
                          @SerializedName("Country.LocalizedName") var CountryLocalizedName: String,
                          @SerializedName("AdministrativeArea.ID") var AdministrativeAreaID: String,
                          @SerializedName(" AdministrativeArea.LocalizedName ") var AdministrativeAreaLocalizedName: String) {
    fun getSuggestion(): SearchSuggestion {
        return StringSearchSuggestion(LocalizedName)
    }
}
