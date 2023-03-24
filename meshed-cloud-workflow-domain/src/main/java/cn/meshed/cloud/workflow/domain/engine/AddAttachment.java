package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.io.Serializable;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class AddAttachment implements Serializable {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 附件类型
     */
    private String attachmentType;
    /**
     * 任务ID
     */
    private String taskId;
    /**
     * 实例ID
     */
    private String processInstanceId;
    /**
     * 附件名称
     */
    private String attachmentName;
    /**
     * 附件详情
     */
    private String attachmentDescription;
    /**
     * 附件URL
     */
    private String url;
}
