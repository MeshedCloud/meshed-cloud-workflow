package cn.meshed.cloud.workflow.domain.flow.ability;

import cn.meshed.cloud.core.ISave;
import cn.meshed.cloud.workflow.flow.command.DesignerCmd;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;

/**
 * <h1>设计能力接口</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface DesignerAbility extends ISave<DesignerCmd, Response> {

    /**
     * 获取设计数据
     *
     * @param flowId 流程ID
     * @return 设计数据
     */
    SingleResponse<String> getDesigner(String flowId);
}
