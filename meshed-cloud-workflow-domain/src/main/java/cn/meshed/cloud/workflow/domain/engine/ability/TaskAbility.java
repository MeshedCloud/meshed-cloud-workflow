package cn.meshed.cloud.workflow.domain.engine.ability;

import cn.meshed.cloud.core.IQuery;
import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.engine.command.AttachmentCmd;
import cn.meshed.cloud.workflow.engine.command.CommentCmd;
import cn.meshed.cloud.workflow.engine.command.CompleteTaskCmd;
import cn.meshed.cloud.workflow.engine.data.AttachmentDTO;
import cn.meshed.cloud.workflow.engine.data.CommentDTO;
import cn.meshed.cloud.workflow.engine.data.TaskActivityDTO;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.query.TaskActivityQry;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import cn.meshed.cloud.workflow.engine.query.TaskQry;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;

/**
 * <h1>任务适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface TaskAbility extends ISearchList<TaskPageQry, PageResponse<TaskDTO>>,
        IQuery<TaskQry, SingleResponse<TaskDTO>> {

    /**
     * 任务活动节点列表
     *
     * @param taskActivityQry 任务活动查询
     * @return {@link MultiResponse<TaskActivityDTO>}
     */
    MultiResponse<TaskActivityDTO> activityList(TaskActivityQry taskActivityQry);
    /**
     * 完成任务
     *
     * @param completeTaskCmd 完成参数
     * @return {@link Response}
     */
    Response completeTask(CompleteTaskCmd completeTaskCmd);

    /**
     * 添加评论
     *
     * @param commentCmd 评论信息
     * @return {@link Response}
     */
    Response addComment(CommentCmd commentCmd);

    /**
     * 根据任务ID查询评论
     *
     * @param processInstanceId 实例ID
     * @return {@link MultiResponse<CommentDTO>}
     */
    MultiResponse<CommentDTO> getComments(String processInstanceId);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     * @return {@link Response}
     */
    Response delComment(String commentId);

    /**
     * 添加附件
     *
     * @param attachmentCmd 添加附件参数
     * @return {@link Response}
     */
    Response addAttachment(AttachmentCmd attachmentCmd);

    /**
     * 获取实例附件
     *
     * @param processInstanceId 实例ID
     * @return {@link MultiResponse<AttachmentDTO>}
     */
    MultiResponse<AttachmentDTO> getAttachments(String processInstanceId);

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     * @return {@link Response}
     */
    Response delAttachment(String attachmentId);
}
