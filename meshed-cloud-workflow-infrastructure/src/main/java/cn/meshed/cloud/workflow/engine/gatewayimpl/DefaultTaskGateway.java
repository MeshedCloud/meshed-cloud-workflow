package cn.meshed.cloud.workflow.engine.gatewayimpl;

import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.workflow.domain.engine.AddAttachment;
import cn.meshed.cloud.workflow.domain.engine.AddComment;
import cn.meshed.cloud.workflow.domain.engine.Attachment;
import cn.meshed.cloud.workflow.domain.engine.Comment;
import cn.meshed.cloud.workflow.domain.engine.CompleteTask;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.TaskService;
import org.flowable.task.api.TaskQuery;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <h1>任务网关默认实现</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultTaskGateway implements TaskGateway {

    private final TaskService taskService;

    /**
     * 完成任务
     *
     * @param completeTask 完成参数
     */
    @Override
    public void completeTask(CompleteTask completeTask) {
        Authentication.setAuthenticatedUserId(completeTask.getUserId());
        taskService.complete(completeTask.getTaskId(), completeTask.getParam());
        Authentication.setAuthenticatedUserId(null);
        addComment(completeTask.getComment());
    }

    /**
     * 添加评论
     *
     * @param addComment 评论信息
     */
    @Override
    public void addComment(AddComment addComment) {
        Authentication.setAuthenticatedUserId(addComment.getUserId());
        taskService.addComment(addComment.getTaskId(), addComment.getProcessInstanceId(),
                addComment.getType(), addComment.getTaskId());
        Authentication.setAuthenticatedUserId(null);
    }

    /**
     * 根据任务ID查询评论
     *
     * @param taskId 任务ID
     * @return {@link List < Comment >}
     */
    @Override
    public List<Comment> getTaskComments(String taskId) {
        List<org.flowable.engine.task.Comment> taskComments = taskService.getTaskComments(taskId);
        return CopyUtils.copyListProperties(taskComments, Comment::new);
    }

    /**
     * 根据任务ID查询评论
     *
     * @param processInstanceId 实例ID
     * @return {@link List<Comment>}
     */
    @Override
    public List<Comment> getInstanceComments(String processInstanceId) {
        List<org.flowable.engine.task.Comment> taskComments = taskService.getProcessInstanceComments(processInstanceId);
        return CopyUtils.copyListProperties(taskComments, Comment::new);
    }

    /**
     * 删除评论
     *
     * @param commentId 评论ID
     */
    @Override
    public void delComment(String commentId) {
        taskService.deleteComment(commentId);
    }

    /**
     * 添加附件
     *
     * @param addAttachment 添加附件参数
     */
    @Override
    public void addAttachment(AddAttachment addAttachment) {
        Authentication.setAuthenticatedUserId(addAttachment.getUserId());
        taskService.createAttachment(addAttachment.getAttachmentType(), addAttachment.getTaskId(),
                addAttachment.getProcessInstanceId(), addAttachment.getAttachmentName(),
                addAttachment.getAttachmentDescription(), addAttachment.getUrl());
        Authentication.setAuthenticatedUserId(null);
    }

    /**
     * 获取任务附件
     *
     * @param taskId 任务ID
     * @return {@link List< Attachment >}
     */
    @Override
    public List<Attachment> getTaskAttachments(String taskId) {
        List<org.flowable.engine.task.Attachment> taskAttachments = taskService.getTaskAttachments(taskId);
        return CopyUtils.copyListProperties(taskAttachments, Attachment::new);
    }

    /**
     * 获取实例附件
     *
     * @param processInstanceId 实例ID
     * @return {@link List<Attachment>}
     */
    @Override
    public List<Attachment> getInstanceAttachments(String processInstanceId) {
        List<org.flowable.engine.task.Attachment> taskAttachments = taskService.getProcessInstanceAttachments(processInstanceId);
        return CopyUtils.copyListProperties(taskAttachments, Attachment::new);
    }

    /**
     * 删除附件
     *
     * @param attachmentId 附件ID
     */
    @Override
    public void delAttachment(String attachmentId) {
        taskService.deleteAttachment(attachmentId);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<Task>}
     */
    @Override
    public PageResponse<Task> searchList(TaskPageQry pageQry) {
        TaskQuery query = getProcessTaskQuery(pageQry);
        //查询总数
        long total = query.count();
        if (total == 0) {
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
        }
        //查询列表
        List<org.flowable.task.api.Task> taskList = query.listPage(pageQry.getOffset(), pageQry.getPageSize());
        List<Task> tasks = CopyUtils.copyListProperties(taskList, Task::new);
        return PageResponse.of(tasks, Math.toIntExact(total), pageQry.getPageIndex(), pageQry.getPageSize());
    }

    /**
     * 查询条件
     *
     * @param pageQry
     * @return
     */
    private TaskQuery getProcessTaskQuery(TaskPageQry pageQry) {
        TaskQuery taskQuery = taskService.createTaskQuery();

        //名字模糊匹配
        if (StringUtils.isNotBlank(pageQry.getName())) {
            taskQuery.taskNameLike(pageQry.getName());
        }
        //分类匹配
        if (StringUtils.isNotBlank(pageQry.getCategory())) {
            taskQuery.taskCategory(pageQry.getCategory());
        }
        //任务人匹配
        if (StringUtils.isNotBlank(pageQry.getAssignee())) {
            taskQuery.taskAssignee(pageQry.getAssignee());
        }
        /*
         * 时间区间处理
         */
        if (Objects.nonNull(pageQry.getCreatedAfter())) {
            taskQuery.taskCreatedAfter(pageQry.getCreatedAfter());
        }
        if (Objects.nonNull(pageQry.getCreatedBefore())) {
            taskQuery.taskCreatedBefore(pageQry.getCreatedBefore());
        }
        return taskQuery;
    }
}
