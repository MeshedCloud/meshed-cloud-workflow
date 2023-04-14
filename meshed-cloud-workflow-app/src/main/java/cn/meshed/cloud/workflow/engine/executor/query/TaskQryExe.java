package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.iam.account.data.UserDTO;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.gateway.HistoryGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.enums.TaskQueryTypeEnum;
import cn.meshed.cloud.workflow.engine.query.TaskQry;
import cn.meshed.cloud.workflow.wrapper.user.UserWrapper;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class TaskQryExe implements QueryExecute<TaskQry, SingleResponse<TaskDTO>> {

    private final TaskGateway taskGateway;
    private final HistoryGateway historyGateway;
    private final UserWrapper userWrapper;

    /**
     * <h1>查询执行器</h1>
     *
     * @param taskQry 执行器 {@link TaskQry}
     * @return {@link TaskDTO}
     */
    @Override
    public SingleResponse<TaskDTO> execute(TaskQry taskQry) {
        Task task = null;
        if (taskQry.getType() == TaskQueryTypeEnum.TODO){
            task = taskGateway.query(taskQry);
        } else {
            task = historyGateway.query(taskQry);
        }
        AssertUtils.isTrue(task != null, "任务信息不存在");
        assert task != null;
        fillInfo(task);
        return ResultUtils.copy(task, TaskDTO.class);
    }

    private void fillInfo(Task task) {
        Set<Long> ids = new HashSet<>();
        String assignee = task.getAssignee();
        if (StringUtils.isNumeric(assignee)){
            ids.add(Long.valueOf(assignee));
        }
        String initiator = task.getInitiator();
        if (StringUtils.isNumeric(initiator)){
            ids.add(Long.valueOf(initiator));
        }

        Map<Long, UserDTO> userMap = userWrapper.getUserMap(ids);
        if (StringUtils.isNumeric(assignee)){
            UserDTO userDTO = userMap.get(Long.valueOf(assignee));
            if (userDTO != null){
                task.setAssignee(userDTO.getName());
            }
        }
        if (StringUtils.isNumeric(initiator)){
            UserDTO userDTO = userMap.get(Long.valueOf(initiator));
            if (userDTO != null){
                task.setInitiator(userDTO.getName());
            }
        }

    }
}
