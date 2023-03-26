package cn.meshed.cloud.workflow.form.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.workflow.form.data.FormOptionDTO;
import cn.meshed.cloud.workflow.form.query.FormSchemaQry;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class FormSelectQryExe implements QueryExecute<Object, SingleResponse<List<FormOptionDTO>>> {

    /**
     * <h1>查询执行器</h1>
     *
     * @param nullParam 无需参数
     * @return {@link SingleResponse<FormOptionDTO>}
     */
    @Override
    public SingleResponse<List<FormOptionDTO>> execute(Object nullParam) {
        return null;
    }
}
