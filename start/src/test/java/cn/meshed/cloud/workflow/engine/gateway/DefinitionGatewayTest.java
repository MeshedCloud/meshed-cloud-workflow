package cn.meshed.cloud.workflow.engine.gateway;

import cn.meshed.cloud.workflow.ProviderApplication;
import cn.meshed.cloud.workflow.domain.engine.Definition;
import cn.meshed.cloud.workflow.domain.engine.gateway.DefinitionGateway;
import cn.meshed.cloud.workflow.engine.query.DefinitionPageQry;
import com.alibaba.cola.dto.PageResponse;
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
public class DefinitionGatewayTest {

    @Autowired
    private DefinitionGateway definitionGateway;

    @Test
    public void invertedState() {

    }

    @Test
    public void searchList() {
        DefinitionPageQry definitionPageQry = new DefinitionPageQry();
        definitionPageQry.setPageIndex(1);
        definitionPageQry.setPageSize(10);
        PageResponse<Definition> list = definitionGateway.searchList(definitionPageQry);
        System.out.println(list.getData());
    }
}