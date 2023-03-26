package cn.meshed.cloud.workflow.domain.flow;

import cn.meshed.cloud.workflow.flow.enums.FormTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
     * 分类
     */
    private String category;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 名称
     */
    private FormTypeEnum formType;

    /**
     * 表单ID
     */
    private String formId;

    /**
     * 表单Url
     */
    private String formUrl;

    /**
     * 详情
     */
    private String description;

    /**
     * 创建日期
     */
    private LocalDate createDate;

}
