package cn.meshed.cloud.workflow.form.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.form.data.FormDTO;
import cn.meshed.cloud.workflow.form.query.FormPageQry;
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
public class FormPageQryExe implements QueryExecute<FormPageQry, PageResponse<FormDTO>> {

    private final FormGateway formGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param pageQry 分页查询参数
     * @return {@link PageResponse<FormDTO>}
     */
    @Override
    public PageResponse<FormDTO> execute(FormPageQry pageQry) {
        return ResultUtils.copyPage(formGateway.searchList(pageQry),FormDTO::new);
    }
}
