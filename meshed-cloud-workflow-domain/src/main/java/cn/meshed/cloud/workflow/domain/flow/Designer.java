package cn.meshed.cloud.workflow.domain.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <h1>设计保存</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Schema(description = "设计")
public class Designer implements Serializable {

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 流程设计图
     */
    private String graph;
}
