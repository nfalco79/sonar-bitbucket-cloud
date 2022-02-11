package com.github.nfalco79.sonar.plugins.bitbucket.cloud;

import java.util.Arrays;
import java.util.Collection;

import org.sonar.api.PropertyType;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.PropertyDefinition;

public class BitbucketCloudProperties {

    public static final String CREDENDIAL_APP_PASSWORD = "App Password";
    public static final String CREDENDIAL_OAUTH2 = "OAuth2 Consumer";

    public static final String PULL_REQUEST_BB_CREDENTIALS = "com.github.nfalco79.bitbucket.credentials.type";
    public static final String PULL_REQUEST_BB_USERNAME = "com.github.nfalco79.bitbucket.username";
    public static final String PULL_REQUEST_BB_SECRET = "com.github.nfalco79.bitbucket.secret.secured";
    public static final String PULL_REQUEST_APPROVE_ENABLED = "com.github.nfalco79.bitbucket.pullrequest.approve.enabled";
    public static final String PULL_REQUEST_BB_URL = "sonar.bitbucket.branch.pullrequest.url";
    private static final String CATEGEGORY = "Bitbucket Integration";

    private Configuration config;

    public BitbucketCloudProperties(Configuration config) {
        this.config = config;
    }

    public boolean approve() {
        return config.getBoolean(PULL_REQUEST_APPROVE_ENABLED).orElse(false);
    }

    public boolean isOAuth2() {
        return CREDENDIAL_OAUTH2.equals(config.get(PULL_REQUEST_BB_CREDENTIALS).orElse(""));
    }

    public String username() {
        return config.get(PULL_REQUEST_BB_USERNAME).orElse(null);
    }

    public String secret() {
        return config.get(PULL_REQUEST_BB_SECRET).orElse(null);
    }

    public static Collection<PropertyDefinition> definitions() {
        return Arrays.asList( //
                PropertyDefinition.builder(PULL_REQUEST_BB_CREDENTIALS) //
                        .category(CATEGEGORY) //
                        .type(PropertyType.SINGLE_SELECT_LIST) //
                        .options(CREDENDIAL_APP_PASSWORD, CREDENDIAL_OAUTH2) //
                        .defaultValue(String.valueOf(CREDENDIAL_APP_PASSWORD)) //
                        .name("Type of credentials") //
                        .index(0) //
                        .build(), //
                PropertyDefinition.builder(PULL_REQUEST_BB_USERNAME) //
                        .category(CATEGEGORY) //
                        .type(PropertyType.STRING) //
                        .defaultValue("") //
                        .name("Username/Key") //
                        .description("User credential to connect to Bitbucket Cloud") //
                        .index(1) //
                        .build(), //
                PropertyDefinition.builder(PULL_REQUEST_BB_SECRET) //
                        .category(CATEGEGORY) //
                        .type(PropertyType.PASSWORD) // secured prefix work only since Sonarqube 9.x
                        .name("Password/Secret") //
                        .defaultValue("") //
                        .description("Secret credential to connect to Bitbucket Cloud") //
                        .index(2) //
                        .build(), //
                PropertyDefinition.builder(PULL_REQUEST_APPROVE_ENABLED) //
                        .category(CATEGEGORY) //
                        .type(PropertyType.BOOLEAN) //
                        .defaultValue(String.valueOf(false)) //
                        .name("Enable approving pull requests") //
                        .description("This approve pull request when quality gate is passed (if implemented).") //
                        .index(3) //
                        .build()
        );
    }
}
