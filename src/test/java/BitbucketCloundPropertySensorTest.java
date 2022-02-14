import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.mockito.Mockito;
import org.sonar.api.batch.sensor.SensorContext;

import com.github.nfalco79.sonar.plugins.bitbucket.cloud.BitbucketCloudProperties;
import com.github.nfalco79.sonar.plugins.bitbucket.cloud.BitbucketCloundPropertySensor;

public class BitbucketCloundPropertySensorTest {

    @Rule
    public final EnvironmentVariables envVars = new EnvironmentVariables();

    @Test
    public void test_autorecognize() {
        String prURL = "https://bitbucket/nfalco79/test-repos";
        envVars.set("CHANGE_URL", prURL);
        BitbucketCloundPropertySensor sensor = new BitbucketCloundPropertySensor();
        SensorContext context = Mockito.mock(SensorContext.class);
        sensor.execute(context);
        Mockito.verify(context).addContextProperty(BitbucketCloudProperties.PULL_REQUEST_BB_URL, prURL);
    }
}
