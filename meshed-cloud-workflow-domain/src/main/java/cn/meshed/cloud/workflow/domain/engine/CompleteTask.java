package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.util.Map;

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
     * 编码ID
     */
    private String taskId;
    /**
     * 实例ID
     */
    private String processInstanceId;
    /**
     * 完成任务携带参数
     */
    private Map<String, Object> param;
    /**
     * 完成携带信息
     */
    private String message;

    /**
     * 获取添加评论数据包
     *
     * @return {@link AddComment}
     */
    public AddComment getComment() {
        AddComment addComment = new AddComment();
        addComment.setTaskId(this.taskId);
        addComment.setMessage(this.message);
        addComment.setType("comment");
        addComment.setProcessInstanceId(this.processInstanceId);
        addComment.setUserId(this.userId);
        return addComment;
    }
}
