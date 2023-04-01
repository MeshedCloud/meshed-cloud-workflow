package cn.meshed.cloud.workflow.flow.web;

import cn.meshed.cloud.workflow.domain.flow.ability.DraftAbility;
import cn.meshed.cloud.workflow.flow.DraftAdapter;
import cn.meshed.cloud.workflow.flow.command.DraftCmd;
import cn.meshed.cloud.workflow.flow.data.DraftDTO;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
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

    private final DraftAbility draftAbility;
    /**
     * 草稿列表
     *
     * @param draftPageQry 草稿分页查询参数
     * @return {@link PageResponse <DraftDTO>}
     */
    @Override
    public PageResponse<DraftDTO> list(@Valid DraftPageQry draftPageQry) {
        return draftAbility.searchList(draftPageQry);
    }

    /**
     * 流程草稿详情
     *
     * @param draftId 草稿ID
     * @return {@link PageResponse<DraftDTO>}
     */
    @Override
    public SingleResponse<DraftDTO> query(String draftId) {
        return draftAbility.query(draftId);
    }

    /**
     * 新增草稿
     *
     * @param draftCmd 新增草稿
     * @return {@link Response}
     */
    @Override
    public SingleResponse<String> save(@Valid DraftCmd draftCmd) {
        return draftAbility.save(draftCmd);
    }

    /**
     * 发布流程
     *
     * @param draftId 草稿ID
     * @return {@link Response}
     */
    @Override
    public Response publish(String draftId) {
        return draftAbility.publish(draftId);
    }

    /**
     * 发布流程
     *
     * @param draftId 草稿ID
     * @return {@link Response}
     */
    @Override
    public Response delete(String draftId) {
        return draftAbility.delete(draftId);
    }
}
