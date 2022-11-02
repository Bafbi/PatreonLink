package fr.bafbi.patreonlink.patreon;

import com.patreon.PatreonOAuth;

import java.io.Serializable;
import java.util.Date;

public class PatreonTokens implements Serializable {

    private final String accessToken;
    private final String refreshToken;
    private final int expiresIn;
    private final String scope;
    private final String tokenType;
    private final Date expiresAt;

    public PatreonTokens() {
        this.accessToken = null;
        this.refreshToken = null;
        this.expiresIn = 0;
        this.scope = null;
        this.tokenType = null;
        this.expiresAt = null;
    }

    public PatreonTokens(String access_token, String refresh_token, int expires_in, String scope, String token_type) {
        this.accessToken = access_token;
        this.refreshToken = refresh_token;
        this.expiresIn = expires_in;
        this.scope = scope;
        this.tokenType = token_type;
        this.expiresAt = new Date(System.currentTimeMillis() + (expires_in * 1000L));
    }

    public PatreonTokens(PatreonOAuth.TokensResponse tokens) {
        this.accessToken = tokens.getAccessToken();
        this.refreshToken = tokens.getRefreshToken();
        this.expiresIn = tokens.getExpiresIn();
        this.scope = tokens.getScope();
        this.tokenType = tokens.getTokenType();
        this.expiresAt = new Date(System.currentTimeMillis() + (expiresIn * 1000L));
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getScope() {
        return scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public PatreonOAuth.TokensResponse toTokensResponse() {
        return new PatreonOAuth.TokensResponse(accessToken, refreshToken, expiresIn, scope, tokenType, expiresAt);
    }


}
