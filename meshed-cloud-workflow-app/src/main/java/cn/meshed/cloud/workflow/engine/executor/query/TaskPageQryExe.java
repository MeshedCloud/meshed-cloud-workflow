package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.gateway.DefinitionGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.HistoryGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

/**
 * <h1>任务分页列表</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class TaskPageQryExe implements QueryExecute<TaskPageQry, PageResponse<TaskDTO>> {

    private final TaskGateway taskGateway;
    private final HistoryGateway historyGateway;
    private final InstanceGateway instanceGateway;
    private final DefinitionGateway definitionGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param taskPageQry 执行器 {@link TaskPageQry}
     * @return {@link PageResponse<TaskDTO>}
     */
    @Override
    public PageResponse<TaskDTO> execute(TaskPageQry taskPageQry) {
        PageResponse<Task> response = new PageResponse<>();
        if (taskPageQry.getType() == null){
            return ResultUtils.copyPage(response, TaskDTO::new);
        }
        switch (taskPageQry.getType()){
            case TODO:
                response = taskGateway.searchList(taskPageQry);
                break;
            case COMPLETE:
                taskPageQry.setFinished(true);
                response = historyGateway.searchList(taskPageQry);
                break;
            case MY_INITIATION:
                response = instanceGateway.searchList(taskPageQry);
                break;
            default:
        }
        PageResponse<TaskDTO> result = ResultUtils.copyPage(response, TaskDTO::new);
        if (CollectionUtils.isNotEmpty(result.getData())) {
            result.getData().forEach(task -> {
                task.setDefinitionName(definitionGateway.getDefinitionName(task.getDefinitionId()));
            });
        }
        return result;
    }
}
