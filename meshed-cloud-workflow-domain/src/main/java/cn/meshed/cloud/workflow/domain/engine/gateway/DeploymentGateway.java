package cn.meshed.cloud.workflow.domain.engine.gateway;

import cn.meshed.cloud.workflow.domain.engine.CreateDeployment;

/**
 * <h1>流程部署网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface DeploymentGateway {

    /**
     * 部署流程
     *
     * @param createDeployment 部署信息
     * @return 部署ID
     */
    String deployment(CreateDeployment createDeployment);

    /**
     * 销毁流程
     *
     * @param deployId 流程部署ID
     * @return 成功与否
     */
    void destroy(String deployId);
}
