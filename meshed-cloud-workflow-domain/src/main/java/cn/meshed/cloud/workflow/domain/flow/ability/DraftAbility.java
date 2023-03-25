package cn.meshed.cloud.workflow.domain.flow.ability;

import cn.meshed.cloud.core.ISave;
import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.flow.command.DraftCmd;
import cn.meshed.cloud.workflow.flow.data.DraftDTO;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.alibaba.cola.dto.Response;

/**
 * <h1>草稿能力</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface DraftAbility extends ISearchList<DraftPageQry, PageResponse<DraftDTO>>, ISave<DraftCmd, Response> {
}
