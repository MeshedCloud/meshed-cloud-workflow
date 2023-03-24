package cn.meshed.cloud.workflow.engine.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.engine.AddAttachment;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.command.AttachmentCmd;
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
public class AttachmentAddExe implements CommandExecute<AttachmentCmd, Response> {

    private final TaskGateway taskGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param attachmentCmd 执行器 {@link AttachmentCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(AttachmentCmd attachmentCmd) {
        AddAttachment addAttachment = CopyUtils.copy(attachmentCmd, AddAttachment.class);
        taskGateway.addAttachment(addAttachment);
        return ResultUtils.ok();
    }
}
