package cn.meshed.cloud.workflow.flow.web;

import cn.meshed.cloud.workflow.domain.flow.ability.FlowAbility;
import cn.meshed.cloud.workflow.flow.FlowAdapter;
import cn.meshed.cloud.workflow.flow.command.InitiateCmd;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class FlowWebAdapter implements FlowAdapter {

    private final FlowAbility flowAbility;

    /**
     * 发起流程
     *
     * @param initiateCmd 流程设计数据
     * @return {@link Response}
     */
    @Override
    public Response initiate(@Valid InitiateCmd initiateCmd) {
        return flowAbility.initiate(initiateCmd);
    }
}
