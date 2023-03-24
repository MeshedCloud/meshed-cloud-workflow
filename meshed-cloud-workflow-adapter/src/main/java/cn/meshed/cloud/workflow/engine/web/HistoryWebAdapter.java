package cn.meshed.cloud.workflow.engine.web;

import cn.meshed.cloud.workflow.domain.engine.ability.HistoryAbility;
import cn.meshed.cloud.workflow.engine.HistoryAdapter;
import cn.meshed.cloud.workflow.engine.command.HistoryClearCmd;
import cn.meshed.cloud.workflow.engine.data.HistoryDTO;
import cn.meshed.cloud.workflow.engine.query.HistoryPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>历史任务适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class HistoryWebAdapter implements HistoryAdapter {

    private final HistoryAbility historyAbility;

    /**
     * 历史任务列表
     *
     * @param historyPageQry 流程实例分页查询参数
     * @return {@link PageResponse < DefinitionDTO >}
     */
    @Override
    public PageResponse<HistoryDTO> list(@Valid HistoryPageQry historyPageQry) {
        return historyAbility.searchList(historyPageQry);
    }

    /**
     * 批量清理历史实例
     *
     * @param historyClearCmd 清理历史命令
     * @return {@link Response}
     */
    @Override
    public Response clear(@Valid HistoryClearCmd historyClearCmd) {
        return historyAbility.clear(historyClearCmd);
    }
}
