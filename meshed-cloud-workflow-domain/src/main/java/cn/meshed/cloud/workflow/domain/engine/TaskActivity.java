package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.util.Date;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class TaskActivity {

    /**
     * 编码
     */
    private String id;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 编码
     */
    private String assigneeId;

    /**
     * 节点附带消息
     */
    private String fullMessage;

    /**
     * 节点审批结束时间
     */
    private Date startTime;

    /**
     * 节点审批结束时间
     */
    private Date endTime;
    /**
     * 用时
     */
    private Long durationInMillis;
}
