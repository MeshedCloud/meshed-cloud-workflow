package cn.meshed.cloud.workflow.flow.executor;

import cn.meshed.cloud.workflow.domain.flow.ability.DraftAbility;
import cn.meshed.cloud.workflow.flow.command.DraftCmd;
import cn.meshed.cloud.workflow.flow.data.DraftDTO;
import cn.meshed.cloud.workflow.flow.executor.command.DraftCmdExe;
import cn.meshed.cloud.workflow.flow.executor.command.DraftDelExe;
import cn.meshed.cloud.workflow.flow.executor.command.DraftPublishCmdExe;
import cn.meshed.cloud.workflow.flow.executor.query.DraftPageQryExe;
import cn.meshed.cloud.workflow.flow.executor.query.DraftQryExe;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.cola.dto.SingleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * <h1>草稿能力实现</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
public class DraftAbilityImpl implements DraftAbility {

    private final DraftCmdExe draftCmdExe;
    private final DraftDelExe draftDelExe;
    private final DraftPublishCmdExe draftPublishCmdExe;
    private final DraftPageQryExe draftPageQryExe;
    private final DraftQryExe draftQryExe;

    /**
     * <h1>保存草稿</h1>
     *
     * @param draftCmd 草稿
     * @return {@link Response}
     */
    @Override
    public SingleResponse<String> save(DraftCmd draftCmd) {
        return draftCmdExe.execute(draftCmd);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry 分页参数
     * @return {@link PageResponse<DraftDTO>}
     */
    @Override
    public PageResponse<DraftDTO> searchList(DraftPageQry pageQry) {
        return draftPageQryExe.execute(pageQry);
    }

    /**
     * 发布流程
     *
     * @param draftId 草稿ID
     * @return {@link Response}
     */
    @Override
    public Response publish(String draftId) {
        return draftPublishCmdExe.execute(draftId);
    }

    /**
     * 查询草稿信息
     *
     * @param draftId 草稿ID
     * @return {@link SingleResponse <DraftDTO>}
     */
    @Override
    public SingleResponse<DraftDTO> query(String draftId) {
        return draftQryExe.execute(draftId);
    }

    /**
     * <h1>删除对象</h1>
     *
     * @param draftId 草稿ID
     * @return {@link Response}
     */
    @Override
    public Response delete(String draftId) {
        return draftDelExe.execute(draftId);
    }
}
