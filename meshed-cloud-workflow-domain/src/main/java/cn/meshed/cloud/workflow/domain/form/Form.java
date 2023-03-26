package cn.meshed.cloud.workflow.domain.form;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.workflow.form.enums.FormStatusEnum;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
public class Form {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 表单key
     */
    @Setter(AccessLevel.NONE)
    private String key;

    /**
     * 表单名称
     */
    private String name;

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
    private FormStatusEnum status;

    public Form() {
        this.version = 1;
    }

    public void setKey(String key) {
        AssertUtils.isTrue(StringUtils.isNotBlank(key),"key不能为空");
        this.key = key.toUpperCase();
    }
}
