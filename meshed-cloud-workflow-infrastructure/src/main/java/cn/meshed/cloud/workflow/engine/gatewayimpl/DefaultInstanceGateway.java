package cn.meshed.cloud.workflow.engine.gatewayimpl;

import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.workflow.domain.engine.InitiateInstance;
import cn.meshed.cloud.workflow.domain.engine.Instance;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.engine.enums.ActiveStatusEnum;
import cn.meshed.cloud.workflow.engine.query.InstancePageQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * <h1>流程实例网关默认实现</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultInstanceGateway implements InstanceGateway {

    private final RuntimeService runtimeService;

    /**
     * 启动流程实例
     *
     * @param initiateInstance 发起实例参数
     * @return 实例ID
     */
    @Override
    public String initiate(InitiateInstance initiateInstance) {
        //设置流程发起人id 流程引擎会对应到变量：INITIATOR
        Authentication.setAuthenticatedUserId(initiateInstance.getInitiator());
        ProcessInstance processInstance = null;
        Map<String, Object> param = initiateInstance.getParam();
        String key = initiateInstance.getKey();
        if (param != null && param.size() > 0) {
            //启动流程
            processInstance = runtimeService.startProcessInstanceByKey(key, param);
        } else {
            //启动流程
            processInstance = runtimeService.startProcessInstanceByKey(key);
        }
        /**
         * 这个方法最终使用一个ThreadLocal类型的变量进行存储，
         * 也就是与当前的线程绑定，所以流程实例启动完毕之后，
         * 需要设置为null，防止多线程的时候出问题。
         */
        Authentication.setAuthenticatedUserId(null);
        return processInstance.getProcessInstanceId();
    }

    /**
     * 销毁实例
     *
     * @param instanceId   实例ID
     * @param deleteReason 删除原因
     */
    @Override
    public void destroy(String instanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(instanceId, deleteReason);
    }

    /**
     * 反转状态
     *
     * @param instanceId 流程定义编码
     */
    @Override
    public void invertedState(String instanceId) {
        ProcessInstance processInstance = getProcessInstance(instanceId);
        //反转操作
        if (processInstance.isSuspended()) {
            runtimeService.activateProcessInstanceById(instanceId);
        } else {
            runtimeService.suspendProcessInstanceById(instanceId);
        }
    }

    /**
     * 查询定义
     *
     * @param instanceId 流程定义编码
     * @return 流程定义
     */
    private ProcessInstance getProcessInstance(String instanceId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(instanceId).singleResult();
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<Instance>}
     */
    @Override
    public PageResponse<Instance> searchList(InstancePageQry pageQry) {
        ProcessInstanceQuery query = getProcessInstanceQuery(pageQry);
        //查询总数
        long total = query.count();
        if (total == 0) {
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
        }
        //查询列表
        List<ProcessInstance> processInstances = query.listPage(pageQry.getOffset(), pageQry.getPageSize());
        List<Instance> instances = CopyUtils.copyListProperties(processInstances, Instance::new);
        return PageResponse.of(instances, Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
    }

    /**
     * 查询条件处理
     *
     * @param pageQry
     * @return
     */
    private ProcessInstanceQuery getProcessInstanceQuery(InstancePageQry pageQry) {
        ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery();
        //名字模糊匹配
        if (StringUtils.isNotBlank(pageQry.getName())) {
            processInstanceQuery.processInstanceNameLikeIgnoreCase(pageQry.getName());
        }
        //状态查询
        if (pageQry.getStatus() == ActiveStatusEnum.ACTIVE) {
            processInstanceQuery.active();
        } else if (pageQry.getStatus() == ActiveStatusEnum.SUSPENDED) {
            processInstanceQuery.suspended();
        }
        //查询指定服务定义下的任务
        if (StringUtils.isNotBlank(pageQry.getDeployId())) {
            processInstanceQuery.deploymentId(pageQry.getDeployId());
        }
        return processInstanceQuery;
    }
}
