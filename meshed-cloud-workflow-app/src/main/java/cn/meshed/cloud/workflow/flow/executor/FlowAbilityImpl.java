package cn.meshed.cloud.workflow.flow.executor;

import cn.meshed.cloud.workflow.domain.flow.ability.FlowAbility;
import cn.meshed.cloud.workflow.flow.command.InitiateCmd;
import cn.meshed.cloud.workflow.flow.executor.command.InitiateCmdExe;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
public class FlowAbilityImpl implements FlowAbility {

    private final InitiateCmdExe initiateCmdExe;

    /**
     * 发起流程
     *
     * @param initiateCmd 发起流程信息
     * @return {@link Response}
     */
    @Override
    public Response initiate(InitiateCmd initiateCmd) {
        return initiateCmdExe.execute(initiateCmd);
    }
}
