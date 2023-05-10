package cn.meshed.cloud.workflow.flow.remote;

import cn.meshed.cloud.workflow.flow.FlowRpc;
import cn.meshed.cloud.workflow.flow.command.InitiateCmd;
import cn.meshed.cloud.workflow.flow.executor.command.InitiateCmdExe;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@DubboService
public class FlowRemote implements FlowRpc {

    private final InitiateCmdExe initiateCmdExe;

    @Override
    public SingleResponse<String> initiate(InitiateCmd initiateCmd) {
        return initiateCmdExe.execute(initiateCmd);
    }
}
