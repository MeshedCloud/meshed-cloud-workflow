package cn.meshed.cloud.workflow.domain.engine;

import lombok.Data;

import java.io.Serializable;

/**
 * <h1>流程定义</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class Definition implements Serializable {

    private String id;

    private String category;

    private String name;

    private String key;

    private String description;

    private Integer version;

    private String resourceName;

    private String deploymentId;

    private String diagramResourceName;

    private Boolean hasStartFormKey;

    private Boolean hasGraphicalNotation;

    private Boolean isSuspended;

    private String tenantId;

    private String derivedFrom;

    private String derivedFromRoot;

    private Integer derivedVersion;

    private String engineVersion;
}
