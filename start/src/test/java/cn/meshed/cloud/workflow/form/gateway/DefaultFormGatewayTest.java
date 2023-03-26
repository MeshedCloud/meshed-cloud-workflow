package cn.meshed.cloud.workflow.form.gateway;

import cn.meshed.cloud.workflow.ProviderApplication;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DefaultFormGatewayTest {

    @Autowired
    private FormGateway formGateway;

    @Test
    public void publish() {
        formGateway.publish("5");
    }

    @Test
    public void copy() {
    }

    @Test
    public void saveSchema() {
    }

    @Test
    public void getSchema() {
    }

    @Test
    public void changeState() {
    }

    @Test
    public void existFormKey() {
    }

    @Test
    public void select() {
    }

    @Test
    public void save() {
    }

    @Test
    public void update() {
    }

    @Test
    public void searchList() {
    }

    @Test
    public void query() {
    }
}