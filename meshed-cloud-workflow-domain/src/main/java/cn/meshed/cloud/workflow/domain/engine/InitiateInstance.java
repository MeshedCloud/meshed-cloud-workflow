package cn.meshed.cloud.workflow.domain.engine;

import cn.meshed.cloud.context.SecurityContext;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Data
public class InitiateInstance implements Serializable {

    /**
     * 发起人
     */
    private String initiator;
    /**
     * 流程标识
     */
    private String key;
    /**
     * 流程定义编码
     */
    private String definitionId;
    /**
     * 发起系统
     */
    private String tenantId;
    /**
     * 携带参数
     */
    private Map<String, Object> param;

    public InitiateInstance() {
        this.initiator = String.valueOf(SecurityContext.getOperatorUserId());
    }
}
