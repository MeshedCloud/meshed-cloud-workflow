package cn.meshed.cloud.workflow.form.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.workflow.form.query.FormSchemaQry;
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
public class FormSchemaQryExe implements QueryExecute<FormSchemaQry, SingleResponse<String>> {
    /**
     * <h1>查询执行器</h1>
     *
     * @param formSchemaQry 执行器 {@link FormSchemaQry}
     * @return {@link SingleResponse<String>}
     */
    @Override
    public SingleResponse<String> execute(FormSchemaQry formSchemaQry) {
        return null;
    }
}
