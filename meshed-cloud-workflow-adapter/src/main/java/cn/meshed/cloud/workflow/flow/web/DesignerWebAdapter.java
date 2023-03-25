package cn.meshed.cloud.workflow.flow.web;

import cn.meshed.cloud.workflow.flow.DesignerAdapter;
import cn.meshed.cloud.workflow.flow.command.DesignerCmd;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>设计器适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class DesignerWebAdapter implements DesignerAdapter {
    /**
     * 流程设计数据
     *
     * @param flowId 流程ID
     * @return {@link SingleResponse <String>}
     */
    @Override
    public SingleResponse<String> getDesigner(String flowId) {
        return null;
    }

    /**
     * 保存流程设计
     *
     * @param designerCmd 流程设计数据
     * @return {@link Response}
     */
    @Override
    public Response save(@Valid DesignerCmd designerCmd) {
        return null;
    }
}
