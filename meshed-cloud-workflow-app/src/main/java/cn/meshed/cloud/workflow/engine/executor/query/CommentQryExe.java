package cn.meshed.cloud.workflow.engine.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.Comment;
import cn.meshed.cloud.workflow.domain.engine.constant.CommentType;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.data.CommentDTO;
import com.alibaba.cola.dto.MultiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
@Slf4j
public class CommentQryExe implements QueryExecute<String, MultiResponse<CommentDTO>> {

    private final TaskGateway taskGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param instanceId 实例ID
     * @return {@link MultiResponse<CommentDTO>}
     */
    @Override
    public MultiResponse<CommentDTO> execute(String instanceId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(instanceId), "实例ID不能为空");
        List<Comment> comments = taskGateway.getComments(instanceId, CommentType.MESSAGE);

        if (CollectionUtils.isNotEmpty(comments)){
            List<CommentDTO> list = comments.stream().map(this::toDTO).collect(Collectors.toList());
            return MultiResponse.of(list);
        }
        return MultiResponse.buildSuccess();
    }

    private CommentDTO toDTO(Comment comment) {
        CommentDTO dto = CopyUtils.copy(comment, CommentDTO.class);
        //todo 转换用户
        dto.setUserName(comment.getUserId());
        return dto;
    }
}
