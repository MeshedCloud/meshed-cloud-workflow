package cn.meshed.cloud.workflow.domain.engine;

import cn.meshed.cloud.workflow.domain.engine.constant.CommentType;
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
    private String id;

    /**
     * 附件名称
     */
    private String attachmentName;

    /**
     * 评论
     */
    private String fullMessage;
    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 附件URL
     */
    private String url;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 时间
     */
    private Date time;


}
