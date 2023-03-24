package cn.meshed.cloud.workflow.domain.engine.ability;

import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.engine.command.HistoryClearCmd;
import cn.meshed.cloud.workflow.engine.data.HistoryDTO;
import cn.meshed.cloud.workflow.engine.query.HistoryPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;

/**
 * <h1>历史任务适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface HistoryAbility extends ISearchList<HistoryPageQry, PageResponse<HistoryDTO>> {

    /**
     * 批量清理历史实例
     *
     * @param historyClearCmd 清理历史命令
     * @return {@link Response}
     */
    Response clear(HistoryClearCmd historyClearCmd);
}
