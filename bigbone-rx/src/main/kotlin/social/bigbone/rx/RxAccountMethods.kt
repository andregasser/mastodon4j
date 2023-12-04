package social.bigbone.rx

import io.reactivex.rxjava3.core.Single
import social.bigbone.MastodonClient
import social.bigbone.api.Pageable
import social.bigbone.api.Range
import social.bigbone.api.entity.Account
import social.bigbone.api.entity.Relationship
import social.bigbone.api.entity.Status
import social.bigbone.api.entity.Token
import social.bigbone.api.method.AccountMethods

/**
 * Reactive implementation of [AccountMethods].
 * Allows access to API methods with endpoints having an "api/vX/accounts" prefix.
 * @see <a href="https://docs.joinmastodon.org/methods/accounts/">Mastodon accounts API methods</a>
 */
class RxAccountMethods(client: MastodonClient) {

    private val accountMethods = AccountMethods(client)

    /**
     * Register an account.
     * @param username The desired usernamem for the account
     * @param email The email address to be used for login
     * @param password The password to be used for login
     * @param agreement Whether the user agrees to the local rules, terms, and policies. These should be presented
     * to the user in order to allow them to consent before setting this parameter to TRUE.
     * @param locale The language of the confirmation email that will be sent
     * @param reason If registrations require manual approval, this text will be reviewed by moderators.
     */
    fun registerAccount(
        username: String,
        email: String,
        password: String,
        agreement: Boolean,
        locale: String,
        reason: String?
    ): Single<Token> = Single.fromCallable {
        accountMethods.registerAccount(username, email, password, agreement, locale, reason).execute()
    }

    /**
     * View information about a profile.
     * @param accountId ID of the profile to look up
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#get">Mastodon API documentation: methods/accounts/#get</a>
     */
    fun getAccount(accountId: String): Single<Account> = Single.fromCallable {
        accountMethods.getAccount(accountId).execute()
    }

    /**
     * Test to make sure that the user token works.
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#verify_credentials">Mastodon API documentation: methods/accounts/#verify_credentials</a>
     */
    fun verifyCredentials(): Single<Account> = Single.fromCallable {
        accountMethods.verifyCredentials().execute()
    }

    /**
     * Update the user’s display and preferences.
     * @param displayName The name to display in the user's profile
     * @param note A new biography for the user
     * @param avatar A String containing a base64-encoded image to display as the user's avatar
     *  (e.g. data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUoAAADrCAYAAAA...)
     * @param header A String containing a base64-encoded image to display as the user's header image
     *  (e.g. data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUoAAADrCAYAAAA...)
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#update_credentials">Mastodon API documentation: methods/accounts/#update_credentials</a>
     */
    fun updateCredentials(displayName: String?, note: String?, avatar: String?, header: String?): Single<Account> =
        Single.fromCallable { accountMethods.updateCredentials(displayName, note, avatar, header).execute() }

    /**
     * Accounts which follow the given account, if network is not hidden by the account owner.
     * @param accountId ID of the account to look up
     * @param range optional Range for the pageable return value
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#followers">Mastodon API documentation: methods/accounts/#followers</a>
     */
    @JvmOverloads
    fun getFollowers(accountId: String, range: Range = Range()): Single<Pageable<Account>> = Single.fromCallable {
        accountMethods.getFollowers(accountId, range).execute()
    }

    /**
     * Accounts which the given account is following, if network is not hidden by the account owner.
     * @param accountId ID of the account to look up
     * @param range optional Range for the pageable return value
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#following">Mastodon API documentation: methods/accounts/#following</a>
     */
    @JvmOverloads
    fun getFollowing(accountId: String, range: Range = Range()): Single<Pageable<Account>> = Single.fromCallable {
        accountMethods.getFollowing(accountId, range).execute()
    }

    /**
     * Statuses posted to the given account.
     * @param accountId ID of the account to look up
     * @param onlyMedia Filter out statuses without attachments.
     * @param excludeReplies Filter out statuses in reply to a different account.
     * @param pinned Filter for pinned statuses only.
     * @param range optional Range for the pageable return value
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#statuses">Mastodon API documentation: methods/accounts/#statuses</a>
     */
    @JvmOverloads
    fun getStatuses(
        accountId: String,
        onlyMedia: Boolean = false,
        excludeReplies: Boolean = false,
        pinned: Boolean = false,
        range: Range = Range()
    ): Single<Pageable<Status>> = Single.fromCallable {
        accountMethods.getStatuses(accountId, onlyMedia, excludeReplies, pinned, range).execute()
    }

    /**
     * Follow the given account. Can also be used to update whether to show reblogs or enable notifications.
     * @param accountId ID of the account to follow
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#follow">Mastodon API documentation: methods/accounts/#follow</a>
     */
    fun followAccount(accountId: String): Single<Relationship> = Single.fromCallable {
        accountMethods.followAccount(accountId).execute()
    }

    /**
     * Unfollow the given account.
     * @param accountId ID of the account to unfollow
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#unfollow">Mastodon API documentation: methods/accounts/#unfollow</a>
     */
    fun unfollowAccount(accountId: String): Single<Relationship> = Single.fromCallable {
        accountMethods.unfollowAccount(accountId).execute()
    }

    /**
     * Block the given account. Clients should filter statuses from this account if received (e.g. due to a boost in the Home timeline)
     * @param accountId ID of the account to block
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#block">Mastodon API documentation: methods/accounts/#block</a>
     */
    fun blockAccount(accountId: String): Single<Relationship> = Single.fromCallable {
        accountMethods.blockAccount(accountId).execute()
    }

    /**
     * Unblock the given account.
     * @param accountId ID of the account to unblock
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#unblock">Mastodon API documentation: methods/accounts/#unblock</a>
     */
    fun unblockAccount(accountId: String): Single<Relationship> = Single.fromCallable {
        accountMethods.unblockAccount(accountId).execute()
    }

    /**
     * Mute the given account. Clients should filter statuses and notifications from this account, if received (e.g. due to a boost in the Home timeline).
     * @param accountId ID of the account to mute
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#mute">Mastodon API documentation: methods/accounts/#mute</a>
     */
    fun muteAccount(accountId: String): Single<Relationship> = Single.fromCallable {
        accountMethods.muteAccount(accountId).execute()
    }

    /**
     * Unmute the given account.
     * @param accountId ID of the account to mute
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#unmute">Mastodon API documentation: methods/accounts/#unmute</a>
     */
    fun unmuteAccount(accountId: String): Single<Relationship> = Single.fromCallable {
        accountMethods.unmuteAccount(accountId).execute()
    }

    /**
     * Find out whether a given account is followed, blocked, muted, etc.
     * @param accountIds List of IDs of the accounts to check
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#relationships">Mastodon API documentation: methods/accounts/#relationships</a>
     */
    fun getRelationships(accountIds: List<String>): Single<List<Relationship>> = Single.fromCallable {
        accountMethods.getRelationships(accountIds).execute()
    }

    /**
     * Search for matching accounts by username or display name.
     * @param query the search query
     * @param limit the maximum number of matching accounts to return (default: 40)
     * @see <a href="https://docs.joinmastodon.org/methods/accounts/#search">Mastodon API documentation: methods/accounts/#search</a>
     */
    @JvmOverloads
    fun searchAccounts(query: String, limit: Int? = null): Single<List<Account>> = Single.fromCallable {
        accountMethods.searchAccounts(query, limit).execute()
    }
}
