package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.AddComment;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.command.CommentCmd;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CommentCmdExe implements CommandExecute<CommentCmd, Response> {

    private final TaskGateway taskGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param commentCmd 执行器 {@link CommentCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(CommentCmd commentCmd) {
        AddComment addComment = CopyUtils.copy(commentCmd, AddComment.class);
        taskGateway.addComment(addComment);
        return ResultUtils.ok();
    }
}
