package cn.meshed.cloud.workflow.domain.engine.gateway;

import cn.meshed.cloud.core.IQuery;
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
public interface DefinitionGateway extends ISearchList<DefinitionPageQry, PageResponse<Definition>>,
        IQuery<String, Definition> {

    /**
     * 反转状态
     *
     * @param definitionId 流程定义编码
     */
    void invertedState(String definitionId);

    /**
     * 获取定义名称
     *
     * @param definitionId 定义编码
     * @return 实例名称
     */
    String getDefinitionName(String definitionId);

}
