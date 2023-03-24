package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.gateway.HistoryGateway;
import cn.meshed.cloud.workflow.engine.command.HistoryClearCmd;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
public class HistoryClearCmdExe implements CommandExecute<HistoryClearCmd, Response> {

    private final HistoryGateway historyGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param historyClearCmd 执行器 {@link HistoryClearCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(HistoryClearCmd historyClearCmd) {
        AssertUtils.isTrue(CollectionUtils.isNotEmpty(historyClearCmd.getInstanceIds()), "删除实例ID不能为空");
        historyGateway.batchClearByProcessInstanceIds(historyClearCmd.getInstanceIds());
        return ResultUtils.ok();
    }
}
