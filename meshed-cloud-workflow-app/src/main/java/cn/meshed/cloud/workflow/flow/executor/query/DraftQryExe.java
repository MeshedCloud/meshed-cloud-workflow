package cn.meshed.cloud.workflow.flow.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.data.DraftDTO;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DraftQryExe implements QueryExecute<String, SingleResponse<DraftDTO>> {

    private final DraftGateway draftGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param draftId 草稿ID
     * @return {@link SingleResponse<String>}
     */
    @Override
    public SingleResponse<DraftDTO> execute(String draftId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(draftId), "草稿ID不能为空");
        return ResultUtils.copy(draftGateway.query(draftId),DraftDTO.class);
    }
}
