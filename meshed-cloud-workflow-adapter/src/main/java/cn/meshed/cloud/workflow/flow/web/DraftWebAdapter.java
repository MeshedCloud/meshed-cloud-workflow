package cn.meshed.cloud.workflow.flow.web;

import cn.meshed.cloud.workflow.flow.DraftAdapter;
import cn.meshed.cloud.workflow.flow.command.DraftCmd;
import cn.meshed.cloud.workflow.flow.data.DraftDTO;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>草稿箱适配器</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@RestController
public class DraftWebAdapter implements DraftAdapter {
    /**
     * 草稿列表
     *
     * @param draftPageQry 草稿分页查询参数
     * @return {@link PageResponse <DraftDTO>}
     */
    @Override
    public PageResponse<DraftDTO> list(@Valid DraftPageQry draftPageQry) {
        return null;
    }

    /**
     * 新增草稿
     *
     * @param draftCmd 新增草稿
     * @return {@link Response}
     */
    @Override
    public Response save(@Valid DraftCmd draftCmd) {
        return null;
    }
}
