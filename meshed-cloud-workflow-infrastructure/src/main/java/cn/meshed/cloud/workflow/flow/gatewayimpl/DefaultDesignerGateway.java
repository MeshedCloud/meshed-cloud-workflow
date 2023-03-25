package cn.meshed.cloud.workflow.flow.gatewayimpl;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import cn.meshed.cloud.workflow.flow.gatewayimpl.database.dataobject.FlowDesignerDO;
import cn.meshed.cloud.workflow.flow.gatewayimpl.database.mapper.FlowDesignerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <h1>持久化默认设计网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component("designerGateway")
@Slf4j
public class DefaultDesignerGateway implements DesignerGateway {

    private final FlowDesignerMapper flowDesignerMapper;

    /**
     * 获取设计数据
     *
     * @param flowId 流程ID
     * @return 设计数据
     */
    @Override
    public String getDesigner(String flowId) {
        FlowDesignerDO flowDesignerDO = flowDesignerMapper.selectById(flowId);
        if (flowDesignerDO == null) {
            return null;
        }
        return flowDesignerDO.getGraph();
    }

    /**
     * <h1>保存对象</h1>
     *
     * @param designer
     * @return {@link Boolean}
     */
    @Override
    public Boolean save(Designer designer) {
        FlowDesignerDO flowDesignerDO = new FlowDesignerDO();
        flowDesignerDO.setGraph(designer.getGraph());
        return flowDesignerMapper.insert(flowDesignerDO) > 0;
    }
}
