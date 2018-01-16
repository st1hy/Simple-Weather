package tomaszgorecki.simpleweather.model

import android.os.Parcel
import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion

data class StringSearchSuggestion(private var string: String): SearchSuggestion {

    constructor(parcel: Parcel) : this(parcel.readString())

    override fun getBody(): String {
        return string
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(string)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StringSearchSuggestion> {
        override fun createFromParcel(parcel: Parcel): StringSearchSuggestion {
            return StringSearchSuggestion(parcel)
        }

        override fun newArray(size: Int): Array<StringSearchSuggestion?> {
            return arrayOfNulls(size)
        }
    }
}