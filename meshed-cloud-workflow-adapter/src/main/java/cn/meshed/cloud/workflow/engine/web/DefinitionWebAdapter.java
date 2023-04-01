package cn.meshed.cloud.workflow.engine.web;

import cn.meshed.cloud.workflow.domain.engine.ability.DefinitionAbility;
import cn.meshed.cloud.workflow.engine.DefinitionAdapter;
import cn.meshed.cloud.workflow.engine.data.DefinitionDTO;
import cn.meshed.cloud.workflow.engine.query.DefinitionPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>流程定义适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class DefinitionWebAdapter implements DefinitionAdapter {

    private final DefinitionAbility definitionAbility;


    /**
     * 流程定义列表
     *
     * @param definitionPageQry 流程定义分页查询参数
     * @return {@link PageResponse<DefinitionDTO>}
     */
    @Override
    public PageResponse<DefinitionDTO> list(@Valid DefinitionPageQry definitionPageQry) {
        return definitionAbility.searchList(definitionPageQry);
    }

    /**
     * 反转状态
     *
     * @param definitionId 流程定义编码
     * @return {@link Response}
     */
    @Override
    public Response invertedState(String definitionId) {
        return definitionAbility.invertedState(definitionId);
    }

    /**
     * 流程定义拷贝
     *
     * @param definitionId 流程定义ID
     * @return {@link Response}
     */
    @Override
    public Response copy(String definitionId) {
        return definitionAbility.copy(definitionId);
    }
}
