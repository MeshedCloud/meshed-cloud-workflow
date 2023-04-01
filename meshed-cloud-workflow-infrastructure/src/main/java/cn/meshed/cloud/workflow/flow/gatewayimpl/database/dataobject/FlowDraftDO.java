package cn.meshed.cloud.workflow.flow.gatewayimpl.database.dataobject;

import cn.meshed.cloud.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author by Vincent Vic
 * @since 2023-03-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_flow_draft")
public class FlowDraftDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 唯一标识
     */
    @TableField("`key`")
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
    @TableField("`name`")
    private String name;

    /**
     * 归属系统
     */
    private String tenantId;

    /**
     * 描述
     */
    private String description;

}
