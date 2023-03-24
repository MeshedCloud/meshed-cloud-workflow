package cn.meshed.cloud.workflow.engine.gatewayimpl;

import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.workflow.domain.engine.History;
import cn.meshed.cloud.workflow.domain.engine.gateway.HistoryGateway;
import cn.meshed.cloud.workflow.engine.query.HistoryPageQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.HistoryService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<History>}
     */
    @Override
    public PageResponse<History> searchList(HistoryPageQry pageQry) {
        HistoricTaskInstanceQuery query = getProcessHistoryQuery(pageQry);
        //查询总数
        long total = query.count();
        if (total == 0) {
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
        }
        //查询列表
        List<HistoricTaskInstance> historicTaskInstances = query.listPage(pageQry.getOffset(), pageQry.getPageSize());
        List<History> histories = CopyUtils.copyListProperties(historicTaskInstances, History::new);
        return PageResponse.of(histories, Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
    }

    /**
     * 查询条件
     *
     * @param pageQry 分页参数
     * @return {@link HistoricTaskInstanceQuery}
     */
    private HistoricTaskInstanceQuery getProcessHistoryQuery(HistoryPageQry pageQry) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery();
        //名字模糊匹配
        if (StringUtils.isNotBlank(pageQry.getName())) {
            historicTaskInstanceQuery.taskNameLike(pageQry.getName());
        }
        //分类模糊匹配
        if (StringUtils.isNotBlank(pageQry.getCategory())) {
            historicTaskInstanceQuery.taskCategory(pageQry.getCategory());
        }
        //任务人匹配
        if (StringUtils.isNotBlank(pageQry.getAssignee())) {
            historicTaskInstanceQuery.taskAssignee(pageQry.getAssignee());
        }
        //任务人匹配
        if (Objects.nonNull(pageQry.getIsFinished()) && pageQry.getIsFinished()) {
            historicTaskInstanceQuery.processFinished();
        } else if (Objects.nonNull(pageQry.getIsFinished()) && !pageQry.getIsFinished()) {
            historicTaskInstanceQuery.processUnfinished();
        }
        /*
         * 时间区间处理
         */
        if (Objects.nonNull(pageQry.getCompletedAfter())) {
            historicTaskInstanceQuery.taskCompletedAfter(pageQry.getCompletedAfter());
        }
        if (Objects.nonNull(pageQry.getCompletedBefore())) {
            historicTaskInstanceQuery.taskCompletedBefore(pageQry.getCompletedBefore());
        }
        historicTaskInstanceQuery.processInstanceId(pageQry.getProcessInstanceId())
                .orderByHistoricTaskInstanceStartTime().desc();
        ;
        return historicTaskInstanceQuery;
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
}
