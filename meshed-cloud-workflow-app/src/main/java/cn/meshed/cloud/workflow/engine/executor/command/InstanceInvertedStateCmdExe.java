package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
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
public class InstanceInvertedStateCmdExe implements CommandExecute<String, Response> {

    private final InstanceGateway instanceGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param instanceId 实例ID
     * @return {@link Response}
     */
    @Override
    public Response execute(String instanceId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(instanceId), "实例ID不能为空");
        instanceGateway.invertedState(instanceId);
        return ResultUtils.ok();
    }
}
