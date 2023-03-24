package cn.meshed.cloud.workflow.domain.engine.ability;

import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.engine.command.InitiateInstanceCmd;
import cn.meshed.cloud.workflow.engine.command.InstanceDestroyCmd;
import cn.meshed.cloud.workflow.engine.data.InstanceDTO;
import cn.meshed.cloud.workflow.engine.query.InstancePageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;

/**
 * <h1>流程实例适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface InstanceAbility extends ISearchList<InstancePageQry, PageResponse<InstanceDTO>> {

    /**
     * 启动流程实例
     *
     * @param initiateInstanceCmd 发起实例参数
     * @return 实例ID
     */
    SingleResponse<String> initiate(InitiateInstanceCmd initiateInstanceCmd);

    /**
     * 销毁实例
     *
     * @param instanceDestroyCmd 销毁命令
     * @return {@link Response}
     */
    Response destroy(InstanceDestroyCmd instanceDestroyCmd);

    /**
     * 反转状态
     *
     * @param instanceId 流程定义编码
     * @return {@link Response}
     */
    Response invertedState(String instanceId);
}
