package cn.meshed.cloud.workflow.engine.gatewayimpl;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.workflow.domain.engine.History;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.gateway.HistoryGateway;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.engine.convert.TaskConvert;
import cn.meshed.cloud.workflow.engine.enums.TaskQueryTypeEnum;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import cn.meshed.cloud.workflow.engine.query.TaskQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <h1>历史网关默认实现</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultHistoryGateway implements HistoryGateway {

    private final HistoryService historyService;
    private final RuntimeService runtimeService;
    private final FormGateway formGateway;

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<History>}
     */
    @Override
    public PageResponse<Task> searchList(TaskPageQry pageQry) {
        HistoricProcessInstanceQuery query = getProcessHistoryQuery(pageQry);
        //查询总数
        long total = query.count();
        if (total == 0) {
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total),
                    pageQry.getPageIndex(), pageQry.getPageSize());
        }
        //查询列表
        List<HistoricProcessInstance> historicProcessInstances = query.listPage(pageQry.getOffset(), pageQry.getPageSize());
        if (CollectionUtils.isEmpty(historicProcessInstances)){
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total),
                    pageQry.getPageIndex(), pageQry.getPageSize());
        }
        List<Task> tasks = historicProcessInstances.stream().map(TaskConvert::toTask).filter(Objects::nonNull).collect(Collectors.toList());
        return PageResponse.of(tasks, Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
    }

    /**
     * 查询条件
     *
     * @param pageQry 分页参数
     * @return {@link HistoricTaskInstanceQuery}
     */
    private HistoricProcessInstanceQuery getProcessHistoryQuery(TaskPageQry pageQry) {
        HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery();
        //名字模糊匹配
        if (StringUtils.isNotBlank(pageQry.getName())) {
            historicProcessInstanceQuery.processInstanceNameLike(pageQry.getName());
        }
        //分类模糊匹配
        if (StringUtils.isNotBlank(pageQry.getCategory())) {
            historicProcessInstanceQuery.processDefinitionCategory(pageQry.getCategory());
        }
        //任务人匹配
        if (StringUtils.isNotBlank(pageQry.getAssignee())) {
            if (pageQry.getType() == TaskQueryTypeEnum.MY){
                historicProcessInstanceQuery.startedBy(pageQry.getAssignee());
            } else {
                historicProcessInstanceQuery.involvedUser(pageQry.getAssignee());
            }

        }

        /*
         * 时间区间处理
         */
        if (Objects.nonNull(pageQry.getCompletedAfter())) {
            historicProcessInstanceQuery.finishedAfter(pageQry.getCompletedAfter());
        }
        if (Objects.nonNull(pageQry.getCompletedBefore())) {
            historicProcessInstanceQuery.finishedBefore(pageQry.getCompletedBefore());
        }
        historicProcessInstanceQuery.orderByProcessInstanceStartTime().desc();
        return historicProcessInstanceQuery;
    }

    /**
     * 批量清理历史实例
     *
     * @param instanceIds 流程实例ID列表
     */
    @Async
    @Override
    public void batchClearByProcessInstanceIds(Set<String> instanceIds) {
        for (String instanceId : instanceIds) {
            historyService.deleteHistoricProcessInstance(instanceId);
        }
    }

    /**
     * 查询
     *
     * @param taskQry
     * @return
     */
    @Override
    public Task query(TaskQry taskQry) {
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(taskQry.getInstanceId()).singleResult();
        AssertUtils.isTrue(historicProcessInstance != null, "任务信息不存在");
        assert historicProcessInstance != null;
        Task task = TaskConvert.toTask(historicProcessInstance);
        task.setEndTime(historicProcessInstance.getEndTime());
        task.setFormKey(formGateway.getStartFormKey(historicProcessInstance.getProcessDefinitionId()));
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(taskQry.getInstanceId()).list();
        if (CollectionUtils.isNotEmpty(variableInstances)){
            Map<String,Object> map = new HashMap<>();
            variableInstances.stream().forEach(variableInstance -> {
                map.put(variableInstance.getVariableName(),variableInstance.getValue());
            });
            task.setVariables(map);
        }

        return task;
    }


}
