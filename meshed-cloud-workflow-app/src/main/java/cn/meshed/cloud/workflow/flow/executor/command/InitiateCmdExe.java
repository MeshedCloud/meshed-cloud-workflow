package cn.meshed.cloud.workflow.flow.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.InitiateInstance;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.flow.command.InitiateCmd;
import com.alibaba.cola.dto.SingleResponse;
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
public class InitiateCmdExe implements CommandExecute<InitiateCmd, SingleResponse<String>> {

    private final InstanceGateway instanceGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param initiateCmd 执行器 {@link InitiateCmd}
     * @return {@link SingleResponse<String>}
     */
    @Override
    public SingleResponse<String> execute(InitiateCmd initiateCmd) {
        InitiateInstance initiateInstance = CopyUtils.copy(initiateCmd, InitiateInstance.class);
        String instanceId = instanceGateway.initiate(initiateInstance);
        return ResultUtils.of(instanceId);
    }
}
