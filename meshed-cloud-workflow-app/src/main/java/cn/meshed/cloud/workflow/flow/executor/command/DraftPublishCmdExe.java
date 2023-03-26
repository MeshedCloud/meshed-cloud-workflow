package cn.meshed.cloud.workflow.flow.executor.command;

import cn.hutool.json.JSONUtil;
import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.IdUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.CreateDeployment;
import cn.meshed.cloud.workflow.domain.engine.gateway.DeploymentGateway;
import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.command.DraftCmd;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static cn.meshed.cloud.workflow.domain.flow.constant.Constants.DRAFT_PREFIX;

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
    private final DesignerGateway designerGateway;
    private final DesignerGateway cacheDesignerGateway;

    private final DeploymentGateway deploymentGateway;

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
        //转换JSON至引擎模型
        CreateDeployment createDeployment = new CreateDeployment();
        assert draft != null;
        createDeployment.setKey(draft.getKey());
        createDeployment.setCategory(draft.getCategory());
        createDeployment.setName(draft.getName());
        createDeployment.setJson(graph);
        String definitionId = deploymentGateway.deployment(createDeployment);
        //存储信息设计信息
        Designer designer = new Designer();
        designer.setFlowId(definitionId);
        designer.setGraph(graph);
        designerGateway.save(designer);

        //TODO 存储扩展信息 （表单）
        return ResultUtils.ok();
    }
}
