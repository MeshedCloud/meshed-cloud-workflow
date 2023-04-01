package cn.meshed.cloud.workflow.domain.flow.gateway;

import cn.meshed.cloud.core.IDelete;
import cn.meshed.cloud.core.ISave;
import cn.meshed.cloud.workflow.domain.flow.Designer;

/**
 * <h1>设计网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface DesignerGateway extends ISave<Designer, Boolean>, IDelete<String,Boolean> {

    /**
     * 获取设计数据
     *
     * @param flowId 流程ID
     * @return 设计数据
     */
    String getDesigner(String flowId);
}
