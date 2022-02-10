package com.github.nfalco79.sonar.plugins.bitbucket.cloud;

import java.util.Arrays;
import java.util.Collection;

import org.sonar.api.PropertyType;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.PropertyDefinition;

public class BitbucketCloudProperties {

    public static final String PULL_REQUEST_APPROVE_ENABLED = "com.github.nfalco79.bitbucket.branch.pullrequest.approve.enabled";
    public static final String PULL_REQUEST_BB_USERNAME = "com.github.nfalco79.bitbucket.branch.pullrequest.username";
    public static final String PULL_REQUEST_BB_SECRET = "com.github.nfalco79.bitbucket.branch.pullrequest.secret.secured";
    public static final String PULL_REQUEST_BB_URL = "sonar.bitbucket.branch.pullrequest.url";
    private static final String CATEGEGORY = "Bitbucket Integration";

    private Configuration config;

    public BitbucketCloudProperties(Configuration config) {
        this.config = config;
    }

    public boolean approve() {
        return config.getBoolean(PULL_REQUEST_APPROVE_ENABLED).orElse(false);
    }

    public String username() {
        return config.get(PULL_REQUEST_BB_USERNAME).orElse(null);
    }

    public String secret() {
        return config.get(PULL_REQUEST_BB_SECRET).orElse(null);
    }

    public static Collection<PropertyDefinition> definitions() {
        return Arrays.asList( //
                PropertyDefinition.builder(PULL_REQUEST_APPROVE_ENABLED) //
                        .category(CATEGEGORY) //
                        .type(PropertyType.BOOLEAN) //
                        .defaultValue(String.valueOf(false)) //
                        .name("Enable approving pull requests") //
                        .description("This approve pull request when quality gate is passed (if implemented).") //
                        .index(0) //
                        .build(), //
                PropertyDefinition.builder(PULL_REQUEST_BB_USERNAME) //
                        .category(CATEGEGORY) //
                        .type(PropertyType.STRING) //
                        .defaultValue("") //
                        .name("User that approve the pull requests") //
                        .description("The user login to approve this pull request") //
                        .index(1) //
                        .build(), //
                PropertyDefinition.builder(PULL_REQUEST_BB_SECRET) //
                        .category(CATEGEGORY) //
                        .type(PropertyType.STRING) //
                        .name("App Password") //
                        .defaultValue("") //
                        .description("The user app password") //
                        .index(2) //
                        .build()
        );
    }
}
