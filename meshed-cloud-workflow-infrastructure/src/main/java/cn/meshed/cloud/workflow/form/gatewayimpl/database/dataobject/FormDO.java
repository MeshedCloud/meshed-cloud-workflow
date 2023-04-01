package cn.meshed.cloud.workflow.form.gatewayimpl.database.dataobject;

import cn.meshed.cloud.entity.BaseEntity;
import cn.meshed.cloud.workflow.form.enums.FormStatusEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author by Vincent Vic
 * @since 2023-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_form")
public class FormDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 表单key
     */
    @TableField("`key`")
    private String key;

    /**
     * 表单名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 表单描述
     */
    private String description;

    /**
     * 表单json数据
     */
    @TableField("`schema`")
    private byte[] schema;

    /**
     * 表单版本
     */
    private Integer version;

    /**
     * 表单状态
     */
    @TableField("`status`")
    private FormStatusEnum status;


}
