package cn.meshed.cloud.workflow.form.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import com.alibaba.cola.dto.Response;
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
public class FormCopyCmdExe implements CommandExecute<String, SingleResponse<String>> {

    private final FormGateway formGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public SingleResponse<String> execute(String formId) {
        AssertUtils.isTrue(StringUtils.isNotBlank(formId), "表单ID不能为空");
        return ResultUtils.of(formGateway.copy(formId));
    }
}
