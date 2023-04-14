package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.iam.account.data.UserDTO;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.gateway.DefinitionGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.HistoryGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import cn.meshed.cloud.workflow.wrapper.user.UserWrapper;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final UserWrapper userWrapper;

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
        Operator operator = SecurityContext.getOperator();
        taskPageQry.setAssignee(operator.getId());
        taskPageQry.setCandidateGroup(operator.getRoles());
        switch (taskPageQry.getType()){
            case TODO:
                response = taskGateway.searchList(taskPageQry);
                break;
            case HISTORY:
            case MY:
                response = historyGateway.searchList(taskPageQry);
                break;
            default:
        }
        PageResponse<TaskDTO> result = ResultUtils.copyPage(response, TaskDTO::new);
        if (CollectionUtils.isNotEmpty(result.getData())) {
            Set<Long> ids = result.getData().stream()
                    .map(TaskDTO::getAssignee).filter(StringUtils::isNumeric).map(Long::parseLong).collect(Collectors.toSet());
            Map<Long, UserDTO> userMap = userWrapper.getUserMap(ids);
            result.getData().forEach(task -> {
                String assignee = task.getAssignee();
                if (StringUtils.isNumeric(assignee)){
                    UserDTO userDTO = userMap.get(Long.parseLong(assignee));
                    if (userDTO != null){
                        task.setAssignee(userDTO.getName());
                    }
                }
            });
        }
        return result;
    }
}
