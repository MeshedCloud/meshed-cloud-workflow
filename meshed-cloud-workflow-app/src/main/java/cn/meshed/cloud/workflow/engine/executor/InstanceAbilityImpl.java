package cn.meshed.cloud.workflow.engine.executor;

import cn.meshed.cloud.workflow.domain.engine.ability.InstanceAbility;
import cn.meshed.cloud.workflow.engine.command.InitiateInstanceCmd;
import cn.meshed.cloud.workflow.engine.command.InstanceDestroyCmd;
import cn.meshed.cloud.workflow.engine.data.InstanceDTO;
import cn.meshed.cloud.workflow.engine.executor.command.InitiateInstanceCmdExe;
import cn.meshed.cloud.workflow.engine.executor.command.InstanceDestroyCmdExe;
import cn.meshed.cloud.workflow.engine.executor.command.InstanceInvertedStateCmdExe;
import cn.meshed.cloud.workflow.engine.executor.query.InstancePageQryExe;
import cn.meshed.cloud.workflow.engine.query.InstancePageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <h1>实例能力显式CQRS</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class InstanceAbilityImpl implements InstanceAbility {

    private final InitiateInstanceCmdExe initiateInstanceCmdExe;
    private final InstanceDestroyCmdExe instanceDestroyCmdExe;
    private final InstanceInvertedStateCmdExe instanceInvertedStateCmdExe;
    private final InstancePageQryExe instancePageQryExe;

    /**
     * 启动流程实例
     *
     * @param initiateInstanceCmd 发起实例参数
     * @return 实例ID
     */
    @Override
    public SingleResponse<String> initiate(InitiateInstanceCmd initiateInstanceCmd) {
        return initiateInstanceCmdExe.execute(initiateInstanceCmd);
    }

    /**
     * 销毁实例
     *
     * @param instanceDestroyCmd 销毁命令
     * @return {@link Response}
     */
    @Override
    public Response destroy(InstanceDestroyCmd instanceDestroyCmd) {
        return instanceDestroyCmdExe.execute(instanceDestroyCmd);
    }

    /**
     * 反转状态
     *
     * @param instanceId 流程定义编码
     * @return {@link Response}
     */
    @Override
    public Response invertedState(String instanceId) {
        return instanceInvertedStateCmdExe.execute(instanceId);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页查询
     * @return {@link PageResponse<InstanceDTO>}
     */
    @Override
    public PageResponse<InstanceDTO> searchList(InstancePageQry pageQry) {
        return instancePageQryExe.execute(pageQry);
    }
}
