package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.Instance;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.engine.data.InstanceDTO;
import cn.meshed.cloud.workflow.engine.query.InstancePageQry;
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
public class InstancePageQryExe implements QueryExecute<InstancePageQry, PageResponse<InstanceDTO>> {

    private final InstanceGateway instanceGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param instancePageQry {@link InstancePageQry}
     * @return {@link PageResponse<Instance>}
     */
    @Override
    public PageResponse<InstanceDTO> execute(InstancePageQry instancePageQry) {
        PageResponse<Instance> response = instanceGateway.searchList(instancePageQry);
        return ResultUtils.copyPage(response, InstanceDTO::new);
    }
}
