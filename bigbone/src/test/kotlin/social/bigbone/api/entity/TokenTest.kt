package social.bigbone.api.entity

import kotlinx.serialization.json.Json
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import social.bigbone.testtool.AssetsUtil

class TokenTest {

    private val jsonSerializer: Json = Json {
        encodeDefaults = true
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    @Test
    fun deserialize() {
        val json = AssetsUtil.readFromAssets("access_token.json")
        val accessToken: Token = jsonSerializer.decodeFromString(json)
        accessToken.accessToken shouldBeEqualTo "test"
        accessToken.tokenType shouldBeEqualTo "bearer"
        accessToken.scope shouldBeEqualTo "read write follow"
        accessToken.createdAt shouldBeEqualTo 1_493_188_835L
    }

    @Test
    fun constructor() {
        val accessToken = Token(accessToken = "123", scope = "scope")
        accessToken.accessToken shouldBeEqualTo "123"
        accessToken.tokenType shouldBeEqualTo ""
        accessToken.scope shouldBeEqualTo "scope"
    }
}
