package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.io.Serializable;

/**
 * <h1>添加评论</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class AddComment implements Serializable {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 实例id
     */
    private String processInstanceId;
    /**
     * 评论类型
     */
    private String type;
    /**
     * 评论信息
     */
    private String message;
}
