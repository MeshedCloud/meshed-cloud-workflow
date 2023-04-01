package cn.meshed.cloud.workflow.form.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.form.data.FormOptionDTO;
import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.SingleResponse;
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
public class FormSelectQryExe implements QueryExecute<Object, MultiResponse<FormOptionDTO>> {

    private final FormGateway formGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param nullParam 无需参数
     * @return {@link SingleResponse<FormOptionDTO>}
     */
    @Override
    public MultiResponse<FormOptionDTO> execute(Object nullParam) {
        return ResultUtils.copyMulti(formGateway.select(), FormOptionDTO::new);
    }
}
