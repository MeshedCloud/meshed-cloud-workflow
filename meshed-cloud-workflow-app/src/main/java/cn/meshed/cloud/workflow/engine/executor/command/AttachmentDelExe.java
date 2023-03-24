package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
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
public class AttachmentDelExe implements CommandExecute<String, Response> {

    private final TaskGateway taskGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param attachmentId 附件ID
     * @return {@link Response}
     */
    @Override
    public Response execute(String attachmentId) {
        taskGateway.delComment(attachmentId);
        return ResultUtils.ok();
    }
}
