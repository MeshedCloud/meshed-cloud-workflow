package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.Definition;
import cn.meshed.cloud.workflow.domain.engine.gateway.DefinitionGateway;
import cn.meshed.cloud.workflow.engine.data.DefinitionDTO;
import cn.meshed.cloud.workflow.engine.query.DefinitionPageQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DefinitionPageQryExe implements QueryExecute<DefinitionPageQry, PageResponse<DefinitionDTO>> {

    private final DefinitionGateway definitionGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param pageQry 执行器 {@link DefinitionPageQry}
     * @return {@link PageResponse<DefinitionDTO>}
     */
    @Override
    public PageResponse<DefinitionDTO> execute(DefinitionPageQry pageQry) {
        PageResponse<Definition> response = definitionGateway.searchList(pageQry);
        return ResultUtils.copyPage(response, DefinitionDTO::new);
    }
}
