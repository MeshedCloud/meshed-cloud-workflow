package cn.meshed.cloud.workflow.domain.engine;

import cn.meshed.cloud.context.SecurityContext;
import cn.meshed.cloud.dto.Operator;
import cn.meshed.cloud.workflow.domain.engine.constant.CommentType;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * <h1>完成任务</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class CompleteTask {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 用户组
     */
    private Set<String> groupIds;
    /**
     * 编码ID
     */
    private String taskId;
    /**
     * 实例ID
     */
    private String instanceId;
    /**
     * 完成任务携带参数
     */
    private Map<String, Object> param;
    /**
     * 完成携带信息
     */
    private String message;

    public CompleteTask() {
        Operator operator = SecurityContext.getOperator();
        this.userId = operator.getId();
        this.groupIds = operator.getRoles();
    }

    /**
     * 获取添加评论数据包
     *
     * @return {@link AddComment}
     */
    public AddComment getComment() {
        AddComment addComment = new AddComment();
        addComment.setTaskId(this.taskId);
        addComment.setMessage(this.message);
        addComment.setType(CommentType.APPROVE);
        addComment.setInstanceId(this.instanceId);
        addComment.setUserId(this.userId);
        return addComment;
    }
}
