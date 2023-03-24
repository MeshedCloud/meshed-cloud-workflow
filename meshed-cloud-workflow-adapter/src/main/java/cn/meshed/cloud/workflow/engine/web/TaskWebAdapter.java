package cn.meshed.cloud.workflow.engine.web;

import cn.meshed.cloud.workflow.domain.engine.ability.TaskAbility;
import cn.meshed.cloud.workflow.engine.TaskAdapter;
import cn.meshed.cloud.workflow.engine.command.AttachmentCmd;
import cn.meshed.cloud.workflow.engine.command.CommentCmd;
import cn.meshed.cloud.workflow.engine.command.CompleteTaskCmd;
import cn.meshed.cloud.workflow.engine.data.AttachmentDTO;
import cn.meshed.cloud.workflow.engine.data.CommentDTO;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <h1>任务适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class TaskWebAdapter implements TaskAdapter {

    private final TaskAbility taskAbility;

    /**
     * 任务列表
     *
     * @param taskPageQry 任务分页查询参数
     * @return {@link PageResponse < DefinitionDTO >>}
     */
    @Override
    public PageResponse<TaskDTO> list(@Valid TaskPageQry taskPageQry) {
        return taskAbility.searchList(taskPageQry);
    }

    /**
     * 完成任务
     *
     * @param completeTaskCmd 完成参数
     * @return {@link Response}
     */
    @Override
    public Response completeTask(@Valid CompleteTaskCmd completeTaskCmd) {
        return taskAbility.completeTask(completeTaskCmd);
    }

    /**
     * 添加评论
     *
     * @param commentCmd 评论信息
     * @return {@link Response}
     */
    @Override
    public Response addComment(@Valid CommentCmd commentCmd) {
        return taskAbility.addComment(commentCmd);
    }

    /**
     * 根据任务ID查询评论
     *
     * @param taskId 任务ID
     * @return {@link SingleResponse<List <CommentDTO>>}
     */
    @Override
    public SingleResponse<List<CommentDTO>> getTaskComments(String taskId) {
        return taskAbility.getTaskComments(taskId);
    }

    /**
     * 根据任务ID查询评论
     *
     * @param instanceId 实例ID
     * @return {@link SingleResponse<List<CommentDTO>>}
     */
    @Override
    public SingleResponse<List<CommentDTO>> getInstanceComments(String instanceId) {
        return taskAbility.getInstanceComments(instanceId);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return {@link Response}
     */
    @Override
    public Response delComment(String commentId) {
        return taskAbility.delComment(commentId);
    }

    /**
     * 添加附件
     *
     * @param attachmentCmd 添加附件参数
     * @return {@link Response}
     */
    @Override
    public Response addAttachment(@Valid AttachmentCmd attachmentCmd) {
        return taskAbility.addAttachment(attachmentCmd);
    }

    /**
     * 获取任务附件
     *
     * @param taskId 任务ID
     * @return {@link SingleResponse<List<AttachmentDTO>>}
     */
    @Override
    public SingleResponse<List<AttachmentDTO>> getTaskAttachments(String taskId) {
        return taskAbility.getTaskAttachments(taskId);
    }

    /**
     * 获取实例附件
     *
     * @param instanceId 实例ID
     * @return {@link SingleResponse<List<AttachmentDTO>>}
     */
    @Override
    public SingleResponse<List<AttachmentDTO>> getInstanceAttachments(String instanceId) {
        return taskAbility.getInstanceAttachments(instanceId);
    }

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     * @return {@link Response}
     */
    @Override
    public Response delAttachment(String attachmentId) {
        return taskAbility.delAttachment(attachmentId);
    }
}
