package com.github.nfalco79.sonar.plugins.bitbucket.cloud;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.System2;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class BitbucketCloundPropertySensor implements Sensor {
    private static final String SENSOR_NAME = "Bitbucket Clound Integration";
    private static final Logger LOGGER = Loggers.get(SENSOR_NAME);

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name(SENSOR_NAME);
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
            LOGGER.info("Find pull request at {}", prURL);
        }
    }

}
