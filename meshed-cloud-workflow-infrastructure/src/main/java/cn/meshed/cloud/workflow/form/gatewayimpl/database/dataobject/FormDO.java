package cn.meshed.cloud.workflow.form.gatewayimpl.database.dataobject;

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
 * @since 2023-03-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("m_form")
public class FormDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 表单key
     */
    private String key;

    /**
     * 表单描述
     */
    private String description;

    /**
     * 表单json数据
     */
    private String schema;

    /**
     * 表单版本
     */
    private Integer version;

    /**
     * 表单状态
     */
    private Boolean status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;


}
