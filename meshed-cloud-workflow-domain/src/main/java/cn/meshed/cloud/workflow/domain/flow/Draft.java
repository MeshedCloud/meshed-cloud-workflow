package cn.meshed.cloud.workflow.domain.flow;

import cn.meshed.cloud.workflow.flow.enums.FormTypeEnum;
import com.alibaba.cola.dto.Command;
import com.alibaba.cola.dto.DTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

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


}
