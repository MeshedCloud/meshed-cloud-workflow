package cn.meshed.cloud.workflow.engine.executor;

import cn.meshed.cloud.workflow.domain.engine.ability.HistoryAbility;
import cn.meshed.cloud.workflow.engine.command.HistoryClearCmd;
import cn.meshed.cloud.workflow.engine.data.HistoryDTO;
import cn.meshed.cloud.workflow.engine.executor.command.HistoryClearCmdExe;
import cn.meshed.cloud.workflow.engine.executor.query.HistoryPageQryExe;
import cn.meshed.cloud.workflow.engine.query.HistoryPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <h1>历史任务能力显式CQRS</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class HistoryAbilityImpl implements HistoryAbility {

    private final HistoryClearCmdExe historyClearCmdExe;
    private final HistoryPageQryExe historyPageQryExe;

    /**
     * 批量清理历史实例
     *
     * @param historyClearCmd 清理历史命令
     * @return {@link Response}
     */
    @Override
    public Response clear(HistoryClearCmd historyClearCmd) {
        return historyClearCmdExe.execute(historyClearCmd);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页查询
     * @return {@link PageResponse<HistoryDTO>}
     */
    @Override
    public PageResponse<HistoryDTO> searchList(HistoryPageQry pageQry) {
        return historyPageQryExe.execute(pageQry);
    }
}
