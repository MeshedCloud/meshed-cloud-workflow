package cn.meshed.cloud.workflow.flow.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
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
public class DesignerQryExe implements QueryExecute<String, SingleResponse<String>> {

    private final DesignerGateway designerGateway;

    @Autowired
    @Qualifier("cacheDesignerGateway")
    private DesignerGateway cacheDesignerGateway;

    @Autowired
    public DesignerQryExe(DesignerGateway designerGateway) {
        this.designerGateway = designerGateway;
    }

    /**
     * <h1>查询执行器</h1>
     *
     * @param flowId 流程ID
     * @return {@link SingleResponse<String>}
     */
    @Override
    public SingleResponse<String> execute(String flowId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(flowId),"流程ID不能为空");
        String designer = null;
        //草稿从缓存中获取
        if (flowId.startsWith(DRAFT_PREFIX)){
            designer = cacheDesignerGateway.getDesigner(flowId);
        } else {
            //非草稿持久化中获取
            designer = cacheDesignerGateway.getDesigner(flowId);
        }
        return ResultUtils.of(designer);
    }
}
