package cn.meshed.cloud.workflow.engine.web;

import cn.meshed.cloud.workflow.domain.engine.ability.InstanceAbility;
import cn.meshed.cloud.workflow.engine.InstanceAdapter;
import cn.meshed.cloud.workflow.engine.command.InitiateInstanceCmd;
import cn.meshed.cloud.workflow.engine.command.InstanceDestroyCmd;
import cn.meshed.cloud.workflow.engine.data.InstanceDTO;
import cn.meshed.cloud.workflow.engine.query.InstancePageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>流程实例适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class InstanceWebAdapter implements InstanceAdapter {

    private final InstanceAbility instanceAbility;

    /**
     * 流程实例列表
     *
     * @param instancePageQry 流程实例分页查询参数
     * @return {@link PageResponse < DefinitionDTO >}
     */
    @Override
    public PageResponse<InstanceDTO> list(@Valid InstancePageQry instancePageQry) {
        return instanceAbility.searchList(instancePageQry);
    }

    /**
     * 启动流程实例
     *
     * @param initiateInstanceCmd 发起实例参数
     * @return 实例ID
     */
    @Override
    public SingleResponse<String> initiate(@Valid InitiateInstanceCmd initiateInstanceCmd) {
        return instanceAbility.initiate(initiateInstanceCmd);
    }

    /**
     * 销毁实例
     *
     * @param instanceDestroyCmd 销毁命令
     * @return {@link Response}
     */
    @Override
    public Response destroy(@Valid InstanceDestroyCmd instanceDestroyCmd) {
        return instanceAbility.destroy(instanceDestroyCmd);
    }

    /**
     * 反转状态
     *
     * @param instanceId 流程定义编码
     * @return {@link Response}
     */
    @Override
    public Response invertedState(String instanceId) {
        return instanceAbility.invertedState(instanceId);
    }
}
