package cn.meshed.cloud.workflow.domain.engine.gateway;

import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.domain.engine.AddAttachment;
import cn.meshed.cloud.workflow.domain.engine.AddComment;
import cn.meshed.cloud.workflow.domain.engine.Attachment;
import cn.meshed.cloud.workflow.domain.engine.Comment;
import cn.meshed.cloud.workflow.domain.engine.CompleteTask;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import com.alibaba.cola.dto.PageResponse;

import java.util.List;

/**
 * <h1>任务网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface TaskGateway extends ISearchList<TaskPageQry, PageResponse<Task>> {

    /**
     * 完成任务
     *
     * @param completeTask 完成参数
     */
    void completeTask(CompleteTask completeTask);

    /**
     * 添加评论
     *
     * @param addComment 评论信息
     */
    void addComment(AddComment addComment);

    /**
     * 根据任务ID查询评论
     *
     * @param taskId 任务ID
     * @return {@link List<Comment>}
     */
    List<Comment> getTaskComments(String taskId);

    /**
     * 根据任务ID查询评论
     *
     * @param processInstanceId 实例ID
     * @return {@link List<Comment>}
     */
    List<Comment> getInstanceComments(String processInstanceId);

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    void delComment(String commentId);

    /**
     * 添加附件
     *
     * @param addAttachment 添加附件参数
     */
    void addAttachment(AddAttachment addAttachment);

    /**
     * 获取任务附件
     *
     * @param taskId 任务ID
     * @return {@link List<Attachment>}
     */
    List<Attachment> getTaskAttachments(String taskId);

    /**
     * 获取实例附件
     *
     * @param processInstanceId 实例ID
     * @return {@link List<Attachment>}
     */
    List<Attachment> getInstanceAttachments(String processInstanceId);

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     */
    void delAttachment(String attachmentId);
}
