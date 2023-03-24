package cn.meshed.cloud.workflow.engine.gatewayimpl;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.workflow.domain.engine.CreateDeployment;
import cn.meshed.cloud.workflow.domain.engine.gateway.DeploymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Component;

/**
 * <h1>默认部署网关实现（flowable）</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultDeploymentGateway implements DeploymentGateway {

    private final RepositoryService repositoryService;

    /**
     * 部署流程
     *
     * @param createDeployment 部署信息
     * @return 部署ID
     */
    @Override
    public String deployment(CreateDeployment createDeployment) {
        //校验
        AssertUtils.isTrue(StringUtils.isNotBlank(createDeployment.getName()), "部署名称不能为空");
        AssertUtils.isTrue(createDeployment.getXmlIn() != null, "部署内容称不能为空");
        //部署信息
        DeploymentBuilder builder = repositoryService.createDeployment();
        builder.addInputStream(createDeployment.getBpmnName(), createDeployment.getXmlIn());
        //svg不为空
        if (createDeployment.getSvgIn() != null) {
            builder.addInputStream(createDeployment.getSvgName(), createDeployment.getSvgIn());
        }
        if (StringUtils.isNotBlank(createDeployment.getCategory())) {
            builder.category(createDeployment.getCategory());
        }
        if (StringUtils.isNotBlank(createDeployment.getKey())) {
            builder.key(createDeployment.getKey());
        }
        Deployment deploy = builder.deploy();
        if (StringUtils.isNotBlank(createDeployment.getCategory())) {
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId()).singleResult();
            repositoryService.setProcessDefinitionCategory(definition.getId(), createDeployment.getCategory());
        }

        return deploy.getId();
    }

    /**
     * 销毁流程
     *
     * @param deployId 流程部署ID
     * @return 成功与否
     */
    @Override
    public void destroy(String deployId) {
        repositoryService.deleteDeployment(deployId, true);
    }
}
