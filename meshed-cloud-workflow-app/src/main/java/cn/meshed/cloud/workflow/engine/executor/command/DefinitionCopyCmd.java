package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.Definition;
import cn.meshed.cloud.workflow.domain.engine.ability.DefinitionAbility;
import cn.meshed.cloud.workflow.domain.engine.gateway.DefinitionGateway;
import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class DefinitionCopyCmd implements CommandExecute<String, Response> {

    private final DefinitionGateway definitionGateway;
    private final DraftGateway draftGateway;
    private final DesignerGateway designerGateway;
    private final DesignerGateway cacheDesignerGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param definitionId 流程定义ID
     * @return {@link Response}
     */
    @Override
    public Response execute(String definitionId) {
        Definition definition = definitionGateway.query(definitionId);
        if (definition == null) {
            return ResultUtils.fail("404","定义不存在");
        }
        //拷贝信息
        Draft draft = new Draft();
        draft.setVersion(definition.getVersion() + 1);
        draft.setName(definition.getName());
        draft.setCategory(definition.getCategory());
        draft.setTenantId(definition.getTenantId());
        draft.setKey(definition.getKey());
        draft.setDescription(definition.getDescription());
        String uuid = draftGateway.save(draft);
        //拷贝设计图
        String graph = designerGateway.getDesigner(definitionId);
        if (StringUtils.isNotBlank(graph)){
            Designer designer = new Designer();
            designer.setFlowId(uuid);
            designer.setGraph(graph);
            cacheDesignerGateway.save(designer);
        }
        return ResultUtils.of(uuid);
    }
}
