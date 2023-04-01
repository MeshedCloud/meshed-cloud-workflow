package cn.meshed.cloud.workflow.domain.flow;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * <h1>草稿保存模型</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class Draft implements Serializable {

    /**
     * 编码
     */
    private String id;

    /**
     * 分类
     */
    private String key;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 分类
     */
    private String category;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 归属系统
     */
    private String tenantId;

    /**
     * 详情
     */
    private String description;

}
