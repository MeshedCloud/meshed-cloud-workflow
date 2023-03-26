package cn.meshed.cloud.workflow.form.executor.command;

import cn.meshed.cloud.cqrs.CommandExecute;
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
public class FormPublishCmdExe implements CommandExecute<String, Response> {
    /**
     * <h1>执行器</h1>
     *
     * @param formId 表单ID
     * @return {@link Response}
     */
    @Override
    public Response execute(String formId) {
        return null;
    }
}
