package cn.meshed.cloud.workflow.flow.gateway;

import cn.meshed.cloud.workflow.ProviderApplication;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.enums.FormTypeEnum;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
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
public class DraftGatewayTest {

    @Autowired
    private DraftGateway draftGateway;

    @Test
    public void save() {
        for (int i = 0; i < 30; i++) {
            Draft draft = new Draft();
            draft.setId("draft_uuid"+i);
            draft.setCategory("Test");
            draft.setDescription("Test");
            draft.setKey("key"+i);
            draft.setName("name"+i);
            draftGateway.save(draft);
        }
    }

    @Test
    public void searchList() {
        DraftPageQry pageQry = new DraftPageQry();
        pageQry.setPageIndex(4);
        pageQry.setPageSize(5);
        PageResponse<Draft> response = draftGateway.searchList(pageQry);
        System.out.println(response.getData());
    }

    @Test
    public void query() {
    }
}