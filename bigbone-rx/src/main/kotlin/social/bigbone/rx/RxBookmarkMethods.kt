package social.bigbone.rx

import io.reactivex.rxjava3.core.Single
import social.bigbone.MastodonClient
import social.bigbone.api.Pageable
import social.bigbone.api.Range
import social.bigbone.api.entity.Status
import social.bigbone.api.method.BookmarkMethods

/**
 * Reactive implementation of [BookmarkMethods].
 * Allows access to API methods with endpoints having an "api/vX/bookmarks" prefix.
 * @see <a href="https://docs.joinmastodon.org/methods/bookmarks/">Mastodon bookmarks API methods</a>
 */
class RxBookmarkMethods(client: MastodonClient) {
    private val bookmarkMethods = BookmarkMethods(client)

    @JvmOverloads
    fun getBookmarks(range: Range = Range()): Single<Pageable<Status>> =
        Single.fromCallable { bookmarkMethods.getBookmarks(range).execute() }
}
