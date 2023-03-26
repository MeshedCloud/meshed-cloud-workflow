package cn.meshed.cloud.workflow.flow.executor.command;

import cn.meshed.cloud.workflow.ProviderApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@SpringBootTest(classes = ProviderApplication.class)
@RunWith(SpringRunner.class)
public class DraftPublishCmdExeTest {

    @Autowired
    private DraftPublishCmdExe draftPublishCmdExe;

    @Test
    public void execute() {
        draftPublishCmdExe.execute("draft_uuid1");
    }
}