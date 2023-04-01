package cn.meshed.cloud.workflow.flow.executor.command;

import cn.meshed.cloud.workflow.ProviderApplication;
import cn.meshed.cloud.workflow.flow.command.DesignerCmd;
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
public class DesignerCmdExeTest {

    @Autowired
    private DesignerCmdExe designerCmdExe;

    @Test
    public void execute() {
        DesignerCmd designerCmd = new DesignerCmd();
        designerCmd.setFlowId("draft_uuid1");
        designerCmd.setGraph("{}");
        designerCmdExe.execute(designerCmd);
    }
}