package cn.meshed.cloud.workflow.flow.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import cn.meshed.cloud.workflow.flow.command.DesignerCmd;
import com.alibaba.cola.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static cn.meshed.cloud.workflow.domain.flow.constant.Constants.DRAFT_PREFIX;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@Component
@Slf4j
public class DesignerCmdExe implements CommandExecute<DesignerCmd, Response> {

    private final DesignerGateway designerGateway;

    @Autowired
    @Qualifier("cacheDesignerGateway")
    private DesignerGateway cacheDesignerGateway;

    @Autowired
    public DesignerCmdExe(DesignerGateway designerGateway) {
        this.designerGateway = designerGateway;
    }

    /**
     * <h1>执行器</h1>
     *
     * @param designerCmd 执行器 {@link DesignerCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(DesignerCmd designerCmd) {
        String flowId = designerCmd.getFlowId();
        AssertUtils.isTrue(StringUtils.isNotBlank(flowId),"流程ID不能为空");
        Designer designer = CopyUtils.copy(designerCmd, Designer.class);
        //草稿存入缓存中，暂存30天
        if (flowId.startsWith(DRAFT_PREFIX)){
            cacheDesignerGateway.save(designer);
        } else {
            //非草稿持久化
            designerGateway.save(designer);
        }
        return ResultUtils.ok();
    }
}
