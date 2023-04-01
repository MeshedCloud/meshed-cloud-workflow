package cn.meshed.cloud.workflow.domain.flow.ability;

import cn.meshed.cloud.workflow.flow.command.InitiateCmd;
import com.alibaba.cola.dto.Response;


/**
 * <h1>流程泛化显示业务</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface FlowAbility {

    /**
     * 发起流程
     *
     * @param initiateCmd 发起流程信息
     * @return {@link Response}
     */
    Response initiate(InitiateCmd initiateCmd);
}
