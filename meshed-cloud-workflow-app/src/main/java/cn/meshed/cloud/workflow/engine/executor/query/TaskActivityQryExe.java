package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.iam.account.data.UserDTO;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.TaskActivity;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.data.TaskActivityDTO;
import cn.meshed.cloud.workflow.engine.query.TaskActivityQry;
import cn.meshed.cloud.workflow.wrapper.user.UserWrapper;
import com.alibaba.cola.dto.MultiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private final UserWrapper userWrapper;

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
        Set<Long> ids = taskActivities.stream()
                .map(TaskActivity::getAssigneeId).filter(StringUtils::isNumeric).map(Long::parseLong).collect(Collectors.toSet());
        Map<Long, UserDTO> userMap = userWrapper.getUserMap(ids);
        List<TaskActivityDTO> list = taskActivities.stream()
                .map(taskActivity -> toDTO(taskActivity, getUser(userMap, taskActivity)))
                .collect(Collectors.toList());
        return MultiResponse.of(list);
    }

    private UserDTO getUser(Map<Long, UserDTO> userMap, TaskActivity taskActivity) {
        String assigneeId = taskActivity.getAssigneeId();
        if (StringUtils.isNumeric(assigneeId)){
            return userMap.get(Long.parseLong(assigneeId));
        }
        return null;
    }

    private TaskActivityDTO toDTO(TaskActivity taskActivity, UserDTO userDTO) {
        TaskActivityDTO dto = CopyUtils.copy(taskActivity, TaskActivityDTO.class);
        if (userDTO != null){
            //转换为用户名
            dto.setAssigneeName(userDTO.getName());
        }

        return dto;
    }
}
