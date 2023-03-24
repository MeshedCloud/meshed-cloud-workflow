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

    private String initiator;
    private String key;
    private Map<String, Object> param;

    public InitiateInstance() {
        this.initiator = String.valueOf(SecurityContext.getOperatorUserId());
    }
}
