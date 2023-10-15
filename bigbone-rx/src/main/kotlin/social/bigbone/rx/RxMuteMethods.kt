package social.bigbone.rx

import io.reactivex.rxjava3.core.Single
import social.bigbone.MastodonClient
import social.bigbone.api.Pageable
import social.bigbone.api.Range
import social.bigbone.api.entity.Account
import social.bigbone.api.method.MuteMethods

/**
 * Reactive implementation of [MuteMethods].
 * Allows access to API methods with endpoints having an "api/vX/mutes" prefix.
 * @see <a href="https://docs.joinmastodon.org/methods/mutes/">Mastodon mutes API methods</a>
 */
class RxMuteMethods(client: MastodonClient) {

    private val muteMethods = MuteMethods(client)

    @JvmOverloads
    fun getMutes(range: Range = Range()): Single<Pageable<Account>> =
        Single.fromCallable(muteMethods.getMutes(range)::execute)
}
