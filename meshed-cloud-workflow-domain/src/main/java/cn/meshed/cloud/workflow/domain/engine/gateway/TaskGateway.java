package cn.meshed.cloud.workflow.domain.engine.gateway;

import cn.meshed.cloud.core.IQuery;
import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.domain.engine.AddAttachment;
import cn.meshed.cloud.workflow.domain.engine.AddComment;
import cn.meshed.cloud.workflow.domain.engine.Attachment;
import cn.meshed.cloud.workflow.domain.engine.Comment;
import cn.meshed.cloud.workflow.domain.engine.CompleteTask;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.TaskActivity;
import cn.meshed.cloud.workflow.domain.engine.constant.CommentType;
import cn.meshed.cloud.workflow.engine.query.TaskActivityQry;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import cn.meshed.cloud.workflow.engine.query.TaskQry;
import com.alibaba.cola.dto.PageResponse;

import java.util.List;

/**
 * <h1>任务网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface TaskGateway extends ISearchList<TaskPageQry, PageResponse<Task>> ,
        IQuery<TaskQry, Task> {

    /**
     * 任务活动节点列表
     *
     * @param taskActivityQry 查询参数
     * @return {@link List<TaskActivity>}
     */
    List<TaskActivity> activityList(TaskActivityQry taskActivityQry);

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
     * @param instanceId 实例ID
     * @param type   评论类型
     * @return {@link List<Comment>}
     */
    List<Comment> getComments(String instanceId, CommentType type);

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
     * 获取实例附件
     *
     * @param processInstanceId 实例ID
     * @return {@link List<Attachment>}
     */
    List<Attachment> getAttachments(String processInstanceId);

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     */
    void delAttachment(String attachmentId);
}
