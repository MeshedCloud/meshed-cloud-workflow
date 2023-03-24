package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.History;
import cn.meshed.cloud.workflow.domain.engine.gateway.HistoryGateway;
import cn.meshed.cloud.workflow.engine.data.HistoryDTO;
import cn.meshed.cloud.workflow.engine.query.HistoryPageQry;
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
public class HistoryPageQryExe implements QueryExecute<HistoryPageQry, PageResponse<HistoryDTO>> {

    private final HistoryGateway historyGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param historyPageQry 执行器 {@link HistoryPageQry}
     * @return {@link PageResponse<HistoryDTO>}
     */
    @Override
    public PageResponse<HistoryDTO> execute(HistoryPageQry historyPageQry) {
        PageResponse<History> response = historyGateway.searchList(historyPageQry);
        return ResultUtils.copyPage(response, HistoryDTO::new);
    }
}
