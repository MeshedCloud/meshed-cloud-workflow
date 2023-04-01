package cn.meshed.cloud.workflow.flow.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.IdUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.flow.Draft;
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
public class DraftCmdExe implements CommandExecute<DraftCmd, SingleResponse<String>> {

    private final DraftGateway draftGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param draftCmd 执行器 {@link DraftCmd}
     * @return {@link Response}
     */
    @Override
    public SingleResponse<String> execute(DraftCmd draftCmd) {
        Draft draft = CopyUtils.copy(draftCmd, Draft.class);
        draft.setVersion(1);
        String uuid = draftGateway.save(draft);
        return ResultUtils.of(uuid);
    }
}
