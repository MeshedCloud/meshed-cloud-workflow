package cn.meshed.cloud.workflow.domain.engine.gateway;

import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import com.alibaba.cola.dto.PageResponse;

import java.util.Set;

/**
 * <h1>历史流程实例网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface HistoryGateway extends ISearchList<TaskPageQry, PageResponse<Task>> {

    /**
     * 批量清理历史实例
     *
     * @param instanceIds 流程实例ID列表
     */
    void batchClearByProcessInstanceIds(Set<String> instanceIds);
}
