package ru.suleymanovtat.androidclient.requests

import com.vk.api.sdk.VKApiManager
import com.vk.api.sdk.VKApiResponseParser
import com.vk.api.sdk.VKMethodCall
import com.vk.api.sdk.exceptions.VKApiIllegalResponseException
import com.vk.api.sdk.internal.ApiCommand
import org.json.JSONException
import org.json.JSONObject
import ru.suleymanovtat.androidclient.model.VKGroup
import ru.suleymanovtat.androidclient.model.VKUser

class VKGroupsCommand(private val uids: IntArray = intArrayOf()) : ApiCommand<List<VKGroup>>() {
    override fun onExecute(manager: VKApiManager): List<VKGroup> {

        if (uids.isEmpty()) {
            val call = VKMethodCall.Builder()
                .method("groups.get")
                .args("extended", 1)
                .version(manager.config.version)
                .build()
            return manager.execute(call, ResponseApiParser())
        } else {
            val result = ArrayList<VKGroup>()
            val chunks = uids.toList().chunked(CHUNK_LIMIT)
            for (chunk in chunks) {
                val call = VKMethodCall.Builder()
                    .method("groups.get")
                    .args("extended", 1)
                    .version(manager.config.version)
                    .build()
                result.addAll(manager.execute(call, ResponseApiParser()))
            }
            return result
        }
    }

    companion object {
        const val CHUNK_LIMIT = 900
    }

    private class ResponseApiParser : VKApiResponseParser<List<VKGroup>> {
        override fun parse(response: String): List<VKGroup> {
            try {
                val json = JSONObject(response).getJSONObject("response")
                val ja = json.getJSONArray("items")
                val r = ArrayList<VKGroup>(ja.length())
                for (i in 0 until ja.length()) {
                    val group = VKGroup.parse(ja.getJSONObject(i))
                    r.add(group)
                }
                return r
            } catch (ex: JSONException) {
                throw VKApiIllegalResponseException(ex)
            }
        }
    }
}