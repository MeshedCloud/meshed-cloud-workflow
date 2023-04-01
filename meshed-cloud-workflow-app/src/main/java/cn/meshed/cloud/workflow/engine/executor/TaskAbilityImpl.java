package cn.meshed.cloud.workflow.engine.executor;

import cn.meshed.cloud.workflow.domain.engine.ability.TaskAbility;
import cn.meshed.cloud.workflow.engine.command.AttachmentCmd;
import cn.meshed.cloud.workflow.engine.command.CommentCmd;
import cn.meshed.cloud.workflow.engine.command.CompleteTaskCmd;
import cn.meshed.cloud.workflow.engine.data.AttachmentDTO;
import cn.meshed.cloud.workflow.engine.data.CommentDTO;
import cn.meshed.cloud.workflow.engine.data.TaskActivityDTO;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.executor.command.AttachmentAddExe;
import cn.meshed.cloud.workflow.engine.executor.command.AttachmentDelExe;
import cn.meshed.cloud.workflow.engine.executor.command.CommentCmdExe;
import cn.meshed.cloud.workflow.engine.executor.command.CommentDelExe;
import cn.meshed.cloud.workflow.engine.executor.command.CompleteTaskCmdExe;
import cn.meshed.cloud.workflow.engine.executor.query.AttachmentQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.CommentQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.TaskActivityQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.TaskPageQryExe;
import cn.meshed.cloud.workflow.engine.executor.query.TaskQryExe;
import cn.meshed.cloud.workflow.engine.query.TaskActivityQry;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import cn.meshed.cloud.workflow.engine.query.TaskQry;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


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
    private final CommentQryExe commentQryExe;
    private final AttachmentAddExe attachmentAddExe;
    private final AttachmentDelExe attachmentDelExe;
    private final AttachmentQryExe attachmentQryExe;
    private final TaskPageQryExe taskPageQryExe;
    private final TaskQryExe taskQryExe;
    private final TaskActivityQryExe taskActivityQryExe;

    /**
     * 任务活动节点列表
     *
     * @param taskActivityQry 任务活动查询参数
     * @return {@link MultiResponse< TaskActivityDTO >}
     */
    @Override
    public MultiResponse<TaskActivityDTO> activityList(TaskActivityQry taskActivityQry) {
        return taskActivityQryExe.execute(taskActivityQry);
    }

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
     * @param instanceId 实例ID
     * @return {@link MultiResponse<CommentDTO>}
     */
    @Override
    public MultiResponse<CommentDTO> getComments(String instanceId) {
        return commentQryExe.execute(instanceId);
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
     * 获取实例附件
     *
     * @param instanceId 实例ID
     * @return {@link MultiResponse<AttachmentDTO>}
     */
    @Override
    public MultiResponse<AttachmentDTO> getAttachments(String instanceId) {
        return attachmentQryExe.execute(instanceId);
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

    /**
     * 查询
     *
     * @param taskQry 任务参数
     * @return {@link SingleResponse<TaskDTO>}
     */
    @Override
    public SingleResponse<TaskDTO> query(TaskQry taskQry) {
        return taskQryExe.execute(taskQry);
    }
}
