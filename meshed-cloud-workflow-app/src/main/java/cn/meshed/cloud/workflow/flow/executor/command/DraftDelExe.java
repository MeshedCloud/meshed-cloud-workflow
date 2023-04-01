package cn.meshed.cloud.workflow.flow.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.IdUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.command.DraftCmd;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static cn.meshed.cloud.workflow.domain.flow.constant.Constants.DRAFT_PREFIX;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DraftDelExe implements CommandExecute<String, Response> {

    private final DraftGateway draftGateway;
    private final DesignerGateway designerGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param draftId 草稿ID
     * @return {@link Response}
     */
    @Override
    public Response execute(String draftId) {
        draftGateway.delete(draftId);
        designerGateway.delete(draftId);
        return ResultUtils.ok();
    }
}
