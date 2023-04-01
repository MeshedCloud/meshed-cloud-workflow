package cn.meshed.cloud.workflow.form.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.ResultUtils;
import cn.meshed.cloud.workflow.domain.form.Form;
import cn.meshed.cloud.workflow.domain.form.gateway.FormGateway;
import cn.meshed.cloud.workflow.form.command.FormCmd;
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
public class FormCmdExe implements CommandExecute<FormCmd, Response> {

    private final FormGateway formGateway;

    /**
     * <h1>执行器</h1>
     *
     * @param formCmd 执行器 {@link FormCmd}
     * @return {@link Response}
     */
    @Override
    public Response execute(FormCmd formCmd) {
        Form form = CopyUtils.copy(formCmd, Form.class);
        return ResultUtils.of(formGateway.save(form));
    }
}
