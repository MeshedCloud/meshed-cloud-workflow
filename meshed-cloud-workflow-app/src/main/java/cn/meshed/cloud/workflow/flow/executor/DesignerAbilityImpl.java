package cn.meshed.cloud.workflow.flow.executor;

import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.ability.DesignerAbility;
import cn.meshed.cloud.workflow.flow.command.DesignerCmd;
import cn.meshed.cloud.workflow.flow.executor.command.DesignerCmdExe;
import cn.meshed.cloud.workflow.flow.executor.query.DesignerQryExe;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
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
public class DesignerAbilityImpl implements DesignerAbility {

    private final DesignerQryExe designerQryExe;
    private final DesignerCmdExe designerCmdExe;
    /**
     * 获取设计数据
     *
     * @param flowId 流程ID
     * @return 设计数据
     */
    @Override
    public SingleResponse<String> getDesigner(String flowId) {
        return designerQryExe.execute(flowId);
    }

    /**
     * <h1>保存对象</h1>
     *
     * @param designerCmd 设计图存储
     * @return {@link Response}
     */
    @Override
    public Response save(DesignerCmd designerCmd) {
        return designerCmdExe.execute(designerCmd);
    }
}
