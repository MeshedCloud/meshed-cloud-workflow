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

    /**
     * 编码
     */
    private String id;

    /**
     * 分类
     */
    private String category;

    /**
     * 名称
     */
    private String name;

    /**
     * 标识
     */
    private String key;

    /**
     * 详情
     */
    private String description;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 部署编码
     */
    private String deploymentId;

    /**
     * 是否存在挂载表单
     */
    private Boolean hasStartFormKey;

    /**
     * 主表单标识 （开始节点挂载）
     */
    private String formKey;

    /**
     * 是否挂起
     */
    private Boolean suspended;

    /**
     * 租户
     */
    private String tenantId;
}
