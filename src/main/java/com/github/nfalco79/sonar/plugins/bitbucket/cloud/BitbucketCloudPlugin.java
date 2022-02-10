package com.github.nfalco79.sonar.plugins.bitbucket.cloud;

import org.sonar.api.Plugin;

public class BitbucketCloudPlugin implements Plugin {

    @Override
    public void define(Plugin.Context context) {
        context.addExtensions( //
                PullRequestPostAnalysisTask.class, //
                BitbucketCloundPropertySensor.class);
        context.addExtensions(BitbucketCloudProperties.definitions());
    }
}
