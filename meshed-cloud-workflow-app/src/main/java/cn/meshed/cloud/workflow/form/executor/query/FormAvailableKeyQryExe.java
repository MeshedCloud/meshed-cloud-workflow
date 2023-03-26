package cn.meshed.cloud.workflow.form.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import com.alibaba.cola.dto.Response;
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
public class FormAvailableKeyQryExe implements QueryExecute<String, Response> {
    /**
     * <h1>查询执行器</h1>
     *
     * @param key 表单key
     * @return {@link Response}
     */
    @Override
    public Response execute(String key) {
        return null;
    }
}
