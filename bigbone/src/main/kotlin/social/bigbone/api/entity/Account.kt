package social.bigbone.api.entity

import com.google.gson.annotations.SerializedName

data class Account(
    @SerializedName("id")
    val id: String = "0",

    @SerializedName("username")
    val username: String = "",

    @SerializedName("acct")
    val acct: String = "",

    @SerializedName("display_name")
    val displayName: String = "",

    @SerializedName("note")
    val note: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("avatar")
    val avatar: String = "",

    @SerializedName("header")
    val header: String = "",

    @SerializedName("locked")
    val isLocked: Boolean = false,

    @SerializedName("created_at")
    val createdAt: String = "",

    @SerializedName("followers_count")
    val followersCount: Int = 0,

    @SerializedName("following_count")
    val followingCount: Int = 0,

    @SerializedName("statuses_count")
    val statusesCount: Int = 0,

    @SerializedName("emojis")
    val emojis: List<Emoji> = emptyList()
)
