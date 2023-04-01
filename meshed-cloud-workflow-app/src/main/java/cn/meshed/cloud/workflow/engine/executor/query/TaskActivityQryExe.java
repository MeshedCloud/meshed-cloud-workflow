package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.TaskActivity;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.data.TaskActivityDTO;
import cn.meshed.cloud.workflow.engine.query.TaskActivityQry;
import com.alibaba.cola.dto.MultiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
public class TaskActivityQryExe implements QueryExecute<TaskActivityQry, MultiResponse<TaskActivityDTO>> {

    private final TaskGateway taskGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param taskActivityQry 实例ID
     * @return {@link MultiResponse<TaskActivityDTO>}
     */
    @Override
    public MultiResponse<TaskActivityDTO> execute(TaskActivityQry taskActivityQry) {
        List<TaskActivity> taskActivities = taskGateway.activityList(taskActivityQry);
        if (CollectionUtils.isEmpty(taskActivities)){
            return ResultUtils.copyMulti(taskActivities, TaskActivityDTO::new);
        }
        List<TaskActivityDTO> list = taskActivities.stream().map(this::toDTO).collect(Collectors.toList());
        return MultiResponse.of(list);
    }

    private TaskActivityDTO toDTO(TaskActivity taskActivity) {
        TaskActivityDTO dto = CopyUtils.copy(taskActivity, TaskActivityDTO.class);
        //转换为用户名
        dto.setAssigneeName(taskActivity.getAssigneeId());
        return dto;
    }
}
