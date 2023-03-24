package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.InitiateInstance;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.engine.command.InitiateInstanceCmd;
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
public class InitiateInstanceCmdExe implements CommandExecute<InitiateInstanceCmd, SingleResponse<String>> {

    private final InstanceGateway instanceGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param initiateInstanceCmd 执行器 {@link InitiateInstanceCmd}
     * @return {@link SingleResponse<String>}
     */
    @Override
    public SingleResponse<String> execute(InitiateInstanceCmd initiateInstanceCmd) {
        InitiateInstance initiateInstance = CopyUtils.copy(initiateInstanceCmd, InitiateInstance.class);
        String instanceId = instanceGateway.initiate(initiateInstance);
        return ResultUtils.of(instanceId);
    }
}
