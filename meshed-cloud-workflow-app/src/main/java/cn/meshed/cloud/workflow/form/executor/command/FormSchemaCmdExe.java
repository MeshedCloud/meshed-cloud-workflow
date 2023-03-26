package cn.meshed.cloud.workflow.form.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
import cn.meshed.cloud.workflow.form.command.FormSchemaCmd;
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
public class FormSchemaCmdExe implements CommandExecute<FormSchemaCmd, Response> {
    /**
     * <h1>执行器</h1>
     *
     * @param cmd 执行器 {@link C}
     * @return {@link R}
     */
    @Override
    public Response execute(FormSchemaCmd cmd) {
        return null;
    }
}
