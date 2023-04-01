package cn.meshed.cloud.workflow.domain.engine;

import cn.meshed.cloud.workflow.domain.engine.constant.CommentType;
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
    private String instanceId;
    /**
     * 评论类型
     */
    private CommentType type;
    /**
     * 评论信息
     */
    private String message;
}
