package cn.meshed.cloud.workflow.domain.engine.gateway;

import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.domain.engine.InitiateInstance;
import cn.meshed.cloud.workflow.domain.engine.Instance;
import cn.meshed.cloud.workflow.engine.query.InstancePageQry;
import com.alibaba.cola.dto.PageResponse;

/**
 * <h1>流程实例网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface InstanceGateway extends ISearchList<InstancePageQry, PageResponse<Instance>> {

    /**
     * 启动流程实例
     *
     * @param initiateInstance 发起实例参数
     * @return 实例ID
     */
    String initiate(InitiateInstance initiateInstance);

    /**
     * 销毁实例
     *
     * @param instanceId   实例ID
     * @param deleteReason 删除原因
     */
    void destroy(String instanceId, String deleteReason);

    /**
     * 反转状态
     *
     * @param instanceId 流程定义编码
     */
    void invertedState(String instanceId);
}
