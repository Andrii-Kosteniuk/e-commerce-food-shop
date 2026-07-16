package com.ecommerce.security.token;

public final class TokenBlocklistKeys {

    public static final String DEFAULT_PREFIX = "blocklist";

    private TokenBlocklistKeys() {}

    public static String key(String configuredPrefix, String tokenId) {
        String prefix = (configuredPrefix != null && !configuredPrefix.isBlank())
                ? configuredPrefix
                : DEFAULT_PREFIX;
        return prefix + ":" + tokenId;
    }

}
