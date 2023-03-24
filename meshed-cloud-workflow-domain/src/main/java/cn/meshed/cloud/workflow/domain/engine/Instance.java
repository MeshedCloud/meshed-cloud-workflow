package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * <h1>流程实例VO</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class Instance implements Serializable {

    /**
     * The id of the process definition of the process instance.
     */
    private String processDefinitionId;

    /**
     * The name of the process definition of the process instance.
     */
    private String processDefinitionName;

    /**
     * The key of the process definition of the process instance.
     */
    private String processDefinitionKey;

    /**
     * The version of the process definition of the process instance.
     */
    private Integer processDefinitionVersion;

    /**
     * The deployment id of the process definition of the process instance.
     */
    private String deploymentId;

    /**
     * The business key of this process instance.
     */
    private String businessKey;

    /**
     * returns true if the process instance is suspended
     */
    private boolean isSuspended;

    /**
     * Returns the process variables if requested in the process instance query
     */
    private Map<String, Object> processVariables;

    /**
     * The tenant identifier of this process instance
     */
    private String tenantId;

    /**
     * Returns the name of this process instance.
     */
    private String name;

    /**
     * Returns the description of this process instance.
     */
    private String description;

    /**
     * Returns the start time of this process instance.
     */
    private Date startTime;

    /**
     * Returns the user id of this process instance.
     */
    private String startUserId;

    /**
     * The unique identifier of the execution.
     */
    private String id;

    /**
     * Id of the root of the execution tree representing the process instance. It is the same as {@link #Id} if this execution is the process instance.
     */
    private String processInstanceId;
}
