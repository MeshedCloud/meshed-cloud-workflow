package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.engine.command.InstanceDestroyCmd;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class InstanceDestroyCmdExe implements CommandExecute<InstanceDestroyCmd, Response> {

    private final InstanceGateway instanceGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param instanceDestroyCmd 执行器 {@link InstanceDestroyCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(InstanceDestroyCmd instanceDestroyCmd) {
        AssertUtils.isTrue(StringUtils.isNotBlank(instanceDestroyCmd.getInstanceId()), "实例ID不能为空");
        AssertUtils.isTrue(StringUtils.isNotBlank(instanceDestroyCmd.getReason()), "销毁理由不能为空");
        instanceGateway.destroy(instanceDestroyCmd.getInstanceId(), instanceDestroyCmd.getReason());
        return ResultUtils.ok();
    }
}
