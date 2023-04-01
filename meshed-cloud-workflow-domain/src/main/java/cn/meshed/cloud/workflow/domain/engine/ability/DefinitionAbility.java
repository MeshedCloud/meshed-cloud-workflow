package cn.meshed.cloud.workflow.domain.engine.ability;

import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.engine.data.DefinitionDTO;
import cn.meshed.cloud.workflow.engine.query.DefinitionPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;

/**
 * <h1>流程定义适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface DefinitionAbility extends ISearchList<DefinitionPageQry, PageResponse<DefinitionDTO>> {

    /**
     * 反转状态
     *
     * @param definitionId 流程定义编码
     * @return {@link Response}
     */
    Response invertedState(String definitionId);

    /**
     * 拷贝定义
     *
     * @param definitionId 定义ID
     * @return {@link Response}
     */
    Response copy(String definitionId);
}
