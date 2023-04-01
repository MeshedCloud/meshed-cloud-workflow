package cn.meshed.cloud.workflow.flow.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.CreateDeployment;
import cn.meshed.cloud.workflow.domain.engine.gateway.DeploymentGateway;
import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.command.DesignerCmd;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DraftPublishCmdExe implements CommandExecute<String, Response> {

    private final DraftGateway draftGateway;
    private final DesignerCmdExe designerCmdExe;
    private final DesignerGateway cacheDesignerGateway;
    private final DeploymentGateway deploymentGateway;
    private final DraftDelExe draftDelExe;

    /**
     * <h1>执行器</h1>
     *
     * @param draftId 草稿ID
     * @return {@link Response}
     */
    @Override
    public Response execute(String draftId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(draftId),"草稿ID不能为空");
        //获取草稿信息
        Draft draft = draftGateway.query(draftId);
        AssertUtils.isTrue(draft != null, "草稿不存在无非进行发布");
        String graph = cacheDesignerGateway.getDesigner(draftId);
        AssertUtils.isTrue(StringUtils.isNotBlank(graph),draftId + " 草稿未设计无非发布");
        //部署至引擎模型
        String definitionId = deploy(draft, graph);
        //存储信息设计信息
        saveDesigner(graph, definitionId);
        //删除草稿
        draftDelExe.execute(draftId);
        return ResultUtils.ok();
    }

    private void saveDesigner(String graph, String definitionId) {
        DesignerCmd designerCmd = new DesignerCmd();
        designerCmd.setFlowId(definitionId);
        designerCmd.setGraph(graph);
        AssertUtils.isTrue(designerCmdExe.execute(designerCmd).isSuccess(),"部署失败");
    }

    private String deploy(Draft draft, String graph) {
        CreateDeployment createDeployment = new CreateDeployment();
        assert draft != null;
        createDeployment.setKey(draft.getKey());
        createDeployment.setCategory(draft.getCategory());
        createDeployment.setName(draft.getName());
        createDeployment.setTenantId(draft.getTenantId());
        createDeployment.setJson(graph);
        return deploymentGateway.deployment(createDeployment);
    }
}
