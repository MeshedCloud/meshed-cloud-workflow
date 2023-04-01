package cn.meshed.cloud.workflow.form.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.form.command.FormSchemaCmd;
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
public class FormSchemaCmdExe implements CommandExecute<FormSchemaCmd, Response> {

    private final FormGateway formGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param formSchemaCmd 执行器 {@link FormSchemaCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(FormSchemaCmd formSchemaCmd) {
        AssertUtils.isTrue(StringUtils.isNotBlank(formSchemaCmd.getId()), "表单ID不能为空");
        AssertUtils.isTrue(StringUtils.isNotBlank(formSchemaCmd.getSchema()), "表单设计数据不能为空");
        return ResultUtils.of(formGateway.saveSchema(formSchemaCmd.getId(), formSchemaCmd.getSchema()));
    }
}
