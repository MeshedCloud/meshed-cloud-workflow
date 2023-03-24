package cn.meshed.cloud.workflow.engine.executor;

import cn.meshed.cloud.workflow.domain.engine.ability.DefinitionAbility;
import cn.meshed.cloud.workflow.engine.data.DefinitionDTO;
import cn.meshed.cloud.workflow.engine.executor.command.DefinitionInvertedStateCmdExe;
import cn.meshed.cloud.workflow.engine.executor.query.DefinitionPageQryExe;
import cn.meshed.cloud.workflow.engine.query.DefinitionPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <h1>定义能力显式CQRS</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefinitionAbilityImpl implements DefinitionAbility {

    private final DefinitionInvertedStateCmdExe definitionInvertedStateCmdExe;
    private final DefinitionPageQryExe definitionPageQryExe;

    /**
     * 反转状态
     *
     * @param definitionId 流程定义编码
     * @return {@link Response}
     */
    @Override
    public Response invertedState(String definitionId) {
        return definitionInvertedStateCmdExe.execute(definitionId);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<DefinitionDTO>}
     */
    @Override
    public PageResponse<DefinitionDTO> searchList(DefinitionPageQry pageQry) {
        return definitionPageQryExe.execute(pageQry);
    }
}
