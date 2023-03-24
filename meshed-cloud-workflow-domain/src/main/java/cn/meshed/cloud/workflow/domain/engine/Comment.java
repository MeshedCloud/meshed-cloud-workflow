package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <h1>ProcessCommentVO或ProcessAttachmentVO结合对象</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class Comment implements Serializable {

    /**
     * ID
     */
    String id;

    /**
     * 附件名称
     */
    String attachmentName;

    /**
     * 评论
     */
    String fullMessage;

    /**
     * 类型：附件类型/评论/完成
     */
    String type;

    /**
     * 任务ID
     */
    String taskId;

    /**
     * 实例ID
     */
    String processInstanceId;

    /**
     * 附件URL
     */
    String url;

    /**
     * 用户ID
     */
    String userId;

    /**
     * 时间
     */
    Date time;


}
