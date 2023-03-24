package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.command.CommentCmd;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class CommentDelExe implements CommandExecute<String, Response> {

    private final TaskGateway taskGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param commentId 执行器 {@link CommentCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(String commentId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(commentId), "评论ID不能为空");
        taskGateway.delComment(commentId);
        return ResultUtils.ok();
    }
}
