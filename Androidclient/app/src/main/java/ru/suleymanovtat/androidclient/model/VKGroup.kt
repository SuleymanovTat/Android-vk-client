package ru.suleymanovtat.androidclient.model

import android.os.Parcel
import org.json.JSONObject

data class VKGroup(
    val id: Int = 0,
    val name: String = "",
    val screenName: String = "",
    val photo200: String = ""
) {
    companion object  {
        fun parse(json: JSONObject) = VKGroup(
            id = json.optInt("id", 0),
            name = json.optString("name", ""),
            screenName = json.optString("screen_ame", ""),
            photo200 = json.optString("photo_200", "")
        )
    }
}

fun VKGroup.parse(json: JSONObject) = VKGroup(
    id = json.optInt("id", 0),
    name = json.optString("name", ""),
    screenName = json.optString("screen_ame", ""),
    photo200 = json.optString("photo_200", "")
)
