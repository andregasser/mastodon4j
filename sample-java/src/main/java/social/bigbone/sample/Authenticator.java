package social.bigbone.sample;

import social.bigbone.MastodonClient;
import social.bigbone.MastodonRequest;
import social.bigbone.api.Scope;
import social.bigbone.api.entity.Application;
import social.bigbone.api.entity.Token;
import social.bigbone.api.exception.BigBoneRequestException;
import social.bigbone.api.method.OAuthMethods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;
import java.util.Scanner;

@SuppressWarnings("PMD.SystemPrintln")
final class Authenticator {
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";

    private Authenticator() {}

    static MastodonClient appRegistrationIfNeeded(final String instanceName, final String credentialFilePath, final boolean useStreaming) throws IOException, BigBoneRequestException {
        final File file = new File(credentialFilePath);
        if (!file.exists()) {
            System.out.println("create $credentialFilePath.");
            file.createNewFile();
        }
        final Properties properties = new Properties();
        System.out.println("load $credentialFilePath.");
        properties.load(Files.newInputStream(file.toPath()));
        if (properties.get(CLIENT_ID) == null) {
            System.out.println("try app registration...");
            final Application application = application(instanceName);
            properties.put(CLIENT_ID, application.getClientId());
            properties.put(CLIENT_SECRET, application.getClientSecret());
            properties.store(Files.newOutputStream(file.toPath()), "app registration");
        } else {
            System.out.println("app registration found...");
        }
        final String clientId = properties.get(CLIENT_ID).toString();
        final String clientSecret = properties.get(CLIENT_SECRET).toString();
        if (properties.get(ACCESS_TOKEN) == null) {
            System.out.println("get access token for $instanceName...");
            System.out.println("please input your email...");
            final String email = System.console().readLine();
            System.out.println("please input your password...");
            final String pass = System.console().readLine();
            final Token accessToken = getAccessToken(instanceName, clientId, clientSecret, email, pass);
            properties.put(ACCESS_TOKEN, accessToken.getAccessToken());
            properties.store(Files.newOutputStream(file.toPath()), "app registration");
        } else {
            System.out.println("access token found...");
        }
        final MastodonClient.Builder builder = new MastodonClient.Builder(instanceName)
            .accessToken(properties.get(ACCESS_TOKEN).toString());
        if (useStreaming) {
            builder.useStreamingApi();
        }
        return builder.build();
    }

    private static Token getAccessToken(final String instanceName, final String clientId, final String clientSecret, final String email, final String password) throws BigBoneRequestException {
        final MastodonClient client = new MastodonClient.Builder(instanceName).build();
        final OAuthMethods oauthMethods = new OAuthMethods(client);
        return oauthMethods.getUserAccessTokenWithPasswordGrant(clientId, clientSecret, new Scope(), REDIRECT_URI, email, password).execute();
    }

    private static Application application(final String instanceName) throws BigBoneRequestException {
        final MastodonClient client = new MastodonClient.Builder(instanceName).build();
        return client.apps().createApp("bigbone-sample-app", REDIRECT_URI, new Scope(), null).execute();
    }

    private static String getAccessTokenByOAuth(final String instanceName, final String clientId, final String clientSecret, final String redirectUri) throws BigBoneRequestException {
        final MastodonClient client = new MastodonClient.Builder(instanceName).build();
        final String url = client.oauth().getOAuthUrl(clientId, new Scope());
        System.out.println("Open authorization page and copy code: " + url);
        System.out.println("Paste code");
        String authCode;
        try (Scanner s = new Scanner(System.in)) {
            authCode = s.nextLine();
        }
        final MastodonRequest<Token> token = client.oauth().getUserAccessTokenWithAuthorizationCodeGrant(
                clientId,
                clientSecret,
                redirectUri,
                authCode);
        return token.execute().getAccessToken();
    }

    public static void main(final String[] args) throws BigBoneRequestException {
        final String instanceName = args[0];
        final String clientId = args[1];
        final String clientSecret = args[2];
        final String redirectUri = args[3];
        System.out.println("Access Token: " + getAccessTokenByOAuth(instanceName, clientId, clientSecret, redirectUri));
    }
}
