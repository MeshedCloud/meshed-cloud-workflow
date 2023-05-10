package cn.meshed.cloud.workflow.engine.gatewayimpl;

import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.workflow.domain.engine.AddAttachment;
import cn.meshed.cloud.workflow.domain.engine.AddComment;
import cn.meshed.cloud.workflow.domain.engine.Attachment;
import cn.meshed.cloud.workflow.domain.engine.Comment;
import cn.meshed.cloud.workflow.domain.engine.CompleteTask;
import cn.meshed.cloud.workflow.domain.engine.Instance;
import cn.meshed.cloud.workflow.domain.engine.Task;
import cn.meshed.cloud.workflow.domain.engine.TaskActivity;
import cn.meshed.cloud.workflow.domain.engine.constant.CommentType;
import cn.meshed.cloud.workflow.domain.engine.gateway.DefinitionGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.InstanceGateway;
import cn.meshed.cloud.workflow.domain.engine.gateway.TaskGateway;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.engine.convert.TaskConvert;
import cn.meshed.cloud.workflow.engine.data.TaskActivityDTO;
import cn.meshed.cloud.workflow.engine.data.TaskDTO;
import cn.meshed.cloud.workflow.engine.query.TaskActivityQry;
import cn.meshed.cloud.workflow.engine.query.TaskPageQry;
import cn.meshed.cloud.workflow.engine.query.TaskQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.impl.identity.Authentication;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.task.api.TaskQuery;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.ACTIVITY_TYPES;

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
    private final InstanceGateway instanceGateway;
    private final HistoryService historyService;
    private final RuntimeService runtimeService;
    private final FormGateway formGateway;
    private final DefinitionGateway definitionGateway;

    /**
     * 任务活动节点列表
     *
     * @param taskActivityQry 查询参数
     * @return {@link List< TaskActivityDTO >}
     */
    @Override
    public List<TaskActivity> activityList(TaskActivityQry taskActivityQry) {
        //查询活动的节点
        List<HistoricActivityInstance> historyProcess = historyService.createHistoricActivityInstanceQuery()
                .activityTypes(ACTIVITY_TYPES)
                .processInstanceId(taskActivityQry.getInstanceId())
                .orderByHistoricActivityInstanceStartTime().asc().list();
        //查询评论
        List<Comment> comments = getComments(taskActivityQry.getInstanceId(), CommentType.APPROVE);

        Map<String, List<Comment>> commentMap = comments.stream()
                .collect(Collectors.groupingBy(comment -> getCommentGroupKey(comment.getTaskId(), comment.getUserId())));
        return historyProcess.stream()
                .map(activityInstance ->
                        toTaskActivity(activityInstance, commentMap.get(getCommentGroupKey(activityInstance.getTaskId(),
                                activityInstance.getAssignee()))))
                .collect(Collectors.toList());
    }

    @NotNull
    private String getCommentGroupKey(String taskId, String userId) {
        return taskId + "#" + userId;
    }

    private TaskActivity toTaskActivity(HistoricActivityInstance activityInstance, List<Comment> comments) {
        TaskActivity taskActivity = CopyUtils.copy(activityInstance, TaskActivity.class);
        taskActivity.setAssigneeId(activityInstance.getAssignee());
        //封装评论
        if (CollectionUtils.isNotEmpty(comments)) {
            taskActivity.setFullMessage(comments.get(0).getFullMessage());
        }
        return taskActivity;
    }

    /**
     * 完成任务
     *
     * @param completeTask 完成参数
     */
    @Override
    public void completeTask(CompleteTask completeTask) {
        Authentication.setAuthenticatedUserId(completeTask.getUserId());
        addComment(completeTask.getComment());
        org.flowable.task.api.Task task = taskService.createTaskQuery()
                .taskId(completeTask.getTaskId())
                .or()
                .taskInvolvedUser(completeTask.getUserId())
                .taskInvolvedGroups(completeTask.getGroupIds())
                .endOr().singleResult();
        AssertUtils.isTrue(task != null, "非审批人无法进行操作");
        //如果没有审批人说明需要领取任务
        assert task != null;
        if (task.getAssignee() == null){
            taskService.claim(completeTask.getTaskId(), completeTask.getUserId());
        }
        if (completeTask.getParam() != null && completeTask.getParam().size() > 0) {
            taskService.complete(completeTask.getTaskId(), completeTask.getParam());
        } else {
            taskService.complete(completeTask.getTaskId());
        }
        Authentication.setAuthenticatedUserId(null);
    }

    /**
     * 添加评论
     *
     * @param addComment 评论信息
     */
    @Override
    public void addComment(AddComment addComment) {
        Authentication.setAuthenticatedUserId(addComment.getUserId());
        taskService.addComment(addComment.getTaskId(), addComment.getInstanceId(),
                addComment.getType().name(), addComment.getMessage());
        Authentication.setAuthenticatedUserId(null);
    }

    /**
     * 根据任务ID查询评论
     *
     * @param instanceId 实例ID
     * @param type       评论类型
     * @return {@link List<Comment>}
     */
    @Override
    public List<Comment> getComments(String instanceId, CommentType type) {
        List<org.flowable.engine.task.Comment> taskComments = taskService
                .getProcessInstanceComments(instanceId, type.name());
        if (CollectionUtils.isEmpty(taskComments)){
            return Collections.emptyList();
        }
        return taskComments.stream().map(this::toComment).collect(Collectors.toList());
    }

    private Comment toComment(org.flowable.engine.task.Comment comment) {
        Comment target = new Comment();
        target.setFullMessage(comment.getFullMessage());
        target.setId(comment.getId());
        target.setTime(comment.getTime());
        target.setUserId(comment.getUserId());
        target.setTaskId(comment.getTaskId());
        return target;
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
     * 获取实例附件
     *
     * @param processInstanceId 实例ID
     * @return {@link List<Attachment>}
     */
    @Override
    public List<Attachment> getAttachments(String processInstanceId) {
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
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total),
                    pageQry.getPageIndex(), pageQry.getPageSize());
        }
        //查询列表
        List<org.flowable.task.api.Task> taskList = query.listPage(pageQry.getOffset(), pageQry.getPageSize());
        if (CollectionUtils.isEmpty(taskList)) {
            return PageResponse.of(Collections.emptyList(), Math.toIntExact(total), pageQry.getPageIndex(),
                    pageQry.getPageSize());
        }
        List<Task> tasks = taskList.stream().map(TaskConvert::toTask).peek(task -> {
            task.setDefinitionName(definitionGateway.getDefinitionName(task.getDefinitionId()));
        }).collect(Collectors.toList());
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
        taskQuery.or();
        //任务人匹配
        if (StringUtils.isNotBlank(pageQry.getAssignee())) {

            //用户和角色都存在
            if (CollectionUtils.isNotEmpty(pageQry.getCandidateGroup())) {
                taskQuery.taskInvolvedGroups(pageQry.getCandidateGroup());
            }
            taskQuery.taskInvolvedUser(pageQry.getAssignee());
        } else if (CollectionUtils.isNotEmpty(pageQry.getCandidateGroup())) {
            //仅角色存在
            taskQuery.taskInvolvedGroups(pageQry.getCandidateGroup());
        }

        taskQuery.endOr();
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

    /**
     * 查询 (仅允许查询自己的可操作的用户信息)
     *
     * @param taskQry 任务参数
     * @return {@link TaskDTO}
     */
    @Override
    public Task query(TaskQry taskQry) {
        org.flowable.task.api.Task apiTask = taskService.createTaskQuery()
                .processInstanceId(taskQry.getInstanceId())
                .singleResult();
        AssertUtils.isTrue(apiTask != null, "任务信息不存在");
        Task task = TaskConvert.toTask(apiTask);
        Instance instance = instanceGateway.query(task.getInstanceId());
        task.setDefinitionName(instance.getProcessDefinitionName());
        task.setInitiator(instance.getStartUserId());
        task.setVariables(instance.getProcessVariables());
        task.setFormKey(formGateway.getStartFormKey(instance.getProcessDefinitionId()));
        task.setVariables(runtimeService.getVariables(apiTask.getExecutionId()));
        return task;
    }
}
