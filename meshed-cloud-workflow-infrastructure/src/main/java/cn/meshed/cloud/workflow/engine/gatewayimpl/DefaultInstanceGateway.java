package cn.meshed.cloud.workflow.engine.gatewayimpl;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.workflow.domain.engine.InitiateInstance;
import cn.meshed.cloud.workflow.domain.engine.Instance;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.engine.convert.TaskConvert;
import cn.meshed.cloud.workflow.engine.enums.ActiveStatusEnum;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.exception.SysException;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceBuilder;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        String key = initiateInstance.getKey();
        AssertUtils.isTrue(StringUtils.isNotBlank(key),"流程标识不能为空");
        AssertUtils.isTrue(StringUtils.isNotBlank(initiateInstance.getTenantId()),"发起系统不能为空");
        //设置流程发起人id 流程引擎会对应到变量：INITIATOR
        Authentication.setAuthenticatedUserId(initiateInstance.getInitiator());
        ProcessInstanceBuilder builder = runtimeService.createProcessInstanceBuilder()
                .tenantId(initiateInstance.getTenantId());
        if (StringUtils.isNotBlank(key)){
            builder.processDefinitionKey(key);
        } else if (StringUtils.isNotBlank(initiateInstance.getDefinitionId())){
            builder.processDefinitionId(initiateInstance.getDefinitionId());
        } else {
            throw new SysException("流程ID和Key都不存在(必须传递一个)");
        }

        Map<String, Object> param = initiateInstance.getParam();
        if (param == null) {
            param = new HashMap<>();
        }
        //允许跳过
        param.put("_FLOWABLE_SKIP_EXPRESSION_ENABLED",true);
        param.put("initiate", JSONObject.toJSONString(param));
        builder.variables(param);
        //流程启动
        ProcessInstance processInstance = builder.start();

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
    public PageResponse<Task> searchList(TaskPageQry pageQry) {
        ProcessInstanceQuery query = getProcessInstanceQuery(pageQry);
        //查询总数
        long total = query.count();
        if (total == 0) {
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total),
                    pageQry.getPageIndex(), pageQry.getPageSize());
        }
        //查询列表
        List<ProcessInstance> processInstances = query.listPage(pageQry.getOffset(), pageQry.getPageSize());
        if (CollectionUtils.isEmpty(processInstances)){
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total),
                    pageQry.getPageIndex(), pageQry.getPageSize());
        }
        List<Task> tasks = processInstances.stream().map(TaskConvert::toTask).collect(Collectors.toList());
        return PageResponse.of(tasks, Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
    }

    /**
     * 查询条件处理
     *
     * @param pageQry
     * @return
     */
    private ProcessInstanceQuery getProcessInstanceQuery(TaskPageQry pageQry) {
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

    /**
     * 查询
     *
     * @param instanceId 实例参数
     * @return {@link Instance}
     */
    @Override
    public Instance query(String instanceId) {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(instanceId).singleResult();
        return CopyUtils.copy(processInstance, Instance.class);
    }
}
