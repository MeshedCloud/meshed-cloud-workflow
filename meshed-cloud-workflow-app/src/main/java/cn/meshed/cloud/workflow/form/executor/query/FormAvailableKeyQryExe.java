package cn.meshed.cloud.workflow.form.executor.query;

import cn.meshed.cloud.cqrs.QueryExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import com.alibaba.cola.dto.Response;
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
public class FormAvailableKeyQryExe implements QueryExecute<String, Response> {

    private final FormGateway formGateway;

    /**
     * <h1>查询执行器</h1>
     *
     * @param key 表单key
     * @return {@link Response}
     */
    @Override
    public Response execute(String key) {
        AssertUtils.isTrue(StringUtils.isNotBlank(key), "表单ID不能为空");
        return ResultUtils.of(!formGateway.existFormKey(key));
    }
}
