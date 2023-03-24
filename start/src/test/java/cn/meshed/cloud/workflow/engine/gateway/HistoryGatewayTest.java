package cn.meshed.cloud.workflow.engine.gateway;

import cn.meshed.cloud.workflow.ProviderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@SpringBootTest(classes = ProviderApplication.class)
@RunWith(SpringRunner.class)
public class HistoryGatewayTest {

    @Test
    public void batchClearByProcessInstanceIds() {
    }
}