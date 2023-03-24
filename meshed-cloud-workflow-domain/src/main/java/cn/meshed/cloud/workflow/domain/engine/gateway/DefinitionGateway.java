package cn.meshed.cloud.workflow.domain.engine.gateway;

import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.domain.engine.Definition;
import cn.meshed.cloud.workflow.engine.query.DefinitionPageQry;
import com.alibaba.cola.dto.PageResponse;

/**
 * <h1>流程定义网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface DefinitionGateway extends ISearchList<DefinitionPageQry, PageResponse<Definition>> {

    /**
     * 反转状态
     *
     * @param definitionId 流程定义编码
     */
    void invertedState(String definitionId);

}
