package cn.meshed.cloud.workflow.engine.executor;

import cn.meshed.cloud.workflow.domain.engine.ability.TaskAbility;
import cn.meshed.cloud.workflow.engine.command.AttachmentCmd;
import cn.meshed.cloud.workflow.engine.command.CommentCmd;
import cn.meshed.cloud.workflow.engine.command.CompleteTaskCmd;
import cn.meshed.cloud.workflow.engine.data.AttachmentDTO;
import cn.meshed.cloud.workflow.engine.data.CommentDTO;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.executor.command.AttachmentAddExe;
import cn.meshed.cloud.workflow.engine.executor.command.AttachmentDelExe;
import cn.meshed.cloud.workflow.engine.executor.command.CommentCmdExe;
import cn.meshed.cloud.workflow.engine.executor.command.CommentDelExe;
import cn.meshed.cloud.workflow.engine.executor.command.CompleteTaskCmdExe;
import cn.meshed.cloud.workflow.engine.executor.query.InstanceAttachmentQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.InstanceCommentQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.TaskAttachmentQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.TaskCommentQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.TaskPageQryExe;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1>任务能力显式CQRS</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class TaskAbilityImpl implements TaskAbility {
    private final CompleteTaskCmdExe completeTaskCmdExe;
    private final CommentCmdExe commentCmdExe;
    private final CommentDelExe commentDelExe;
    private final TaskCommentQryExe taskCommentQryExe;
    private final InstanceCommentQryExe instanceCommentQryExe;
    private final AttachmentAddExe attachmentAddExe;
    private final AttachmentDelExe attachmentDelExe;
    private final TaskAttachmentQryExe taskAttachmentQryExe;
    private final InstanceAttachmentQryExe instanceAttachmentQryExe;
    private final TaskPageQryExe taskPageQryExe;

    /**
     * 完成任务
     *
     * @param completeTaskCmd 完成参数
     * @return {@link Response}
     */
    @Override
    public Response completeTask(CompleteTaskCmd completeTaskCmd) {
        return completeTaskCmdExe.execute(completeTaskCmd);
    }

    /**
     * 添加评论
     *
     * @param commentCmd 评论信息
     * @return {@link Response}
     */
    @Override
    public Response addComment(CommentCmd commentCmd) {
        return commentCmdExe.execute(commentCmd);
    }

    /**
     * 根据任务ID查询评论
     *
     * @param taskId 任务ID
     * @return {@link SingleResponse < List  < CommentDTO >>}
     */
    @Override
    public SingleResponse<List<CommentDTO>> getTaskComments(String taskId) {
        return taskCommentQryExe.execute(taskId);
    }

    /**
     * 根据任务ID查询评论
     *
     * @param instanceId 实例ID
     * @return {@link SingleResponse<List<CommentDTO>>}
     */
    @Override
    public SingleResponse<List<CommentDTO>> getInstanceComments(String instanceId) {
        return instanceCommentQryExe.execute(instanceId);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return {@link Response}
     */
    @Override
    public Response delComment(String commentId) {
        return commentDelExe.execute(commentId);
    }

    /**
     * 添加附件
     *
     * @param attachmentCmd 添加附件参数
     * @return {@link Response}
     */
    @Override
    public Response addAttachment(AttachmentCmd attachmentCmd) {
        return attachmentAddExe.execute(attachmentCmd);
    }

    /**
     * 获取任务附件
     *
     * @param taskId 任务ID
     * @return {@link SingleResponse<List< AttachmentDTO >>}
     */
    @Override
    public SingleResponse<List<AttachmentDTO>> getTaskAttachments(String taskId) {
        return taskAttachmentQryExe.execute(taskId);
    }

    /**
     * 获取实例附件
     *
     * @param instanceId 实例ID
     * @return {@link SingleResponse<List<AttachmentDTO>>}
     */
    @Override
    public SingleResponse<List<AttachmentDTO>> getInstanceAttachments(String instanceId) {
        return instanceAttachmentQryExe.execute(instanceId);
    }

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     * @return {@link Response}
     */
    @Override
    public Response delAttachment(String attachmentId) {
        return attachmentDelExe.execute(attachmentId);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<TaskDTO>}
     */
    @Override
    public PageResponse<TaskDTO> searchList(TaskPageQry pageQry) {
        return taskPageQryExe.execute(pageQry);
    }
}
