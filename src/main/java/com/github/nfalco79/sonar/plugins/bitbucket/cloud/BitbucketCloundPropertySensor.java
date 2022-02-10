package com.github.nfalco79.sonar.plugins.bitbucket.cloud;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.System2;

public class BitbucketCloundPropertySensor implements Sensor {

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("Bitbucket Clound Integration");
    }

    @Override
    public void execute(SensorContext context) {
        System2 systemUtil = System2.INSTANCE;
        String prURL = systemUtil.envVariable("CHANGE_URL");
        if (prURL == null) {
            prURL = systemUtil.property(BitbucketCloudProperties.PULL_REQUEST_BB_URL);
        }
        if (prURL != null) {
            context.addContextProperty(BitbucketCloudProperties.PULL_REQUEST_BB_URL, prURL);
        }
    }

}
