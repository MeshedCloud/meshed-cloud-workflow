package cn.meshed.cloud.workflow.flow.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.data.DraftDTO;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DraftPageQryExe implements QueryExecute<DraftPageQry, PageResponse<DraftDTO>> {

    private final DraftGateway draftGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param pageQry 分页参数 {@link DraftPageQry}
     * @return {@link PageResponse<DraftDTO>}
     */
    @Override
    public PageResponse<DraftDTO> execute(DraftPageQry pageQry) {
        PageResponse<Draft> response = draftGateway.searchList(pageQry);
        return ResultUtils.copyPage(response, DraftDTO::new);
    }
}
