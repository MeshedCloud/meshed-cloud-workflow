package cn.meshed.cloud.workflow.domain.flow.gateway;

import cn.meshed.cloud.core.IQuery;
import cn.meshed.cloud.core.ISave;
import cn.meshed.cloud.core.ISearchList;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;

/**
 * <h1>草稿网关</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface DraftGateway extends ISearchList<DraftPageQry, PageResponse<Draft>>, ISave<Draft, Boolean>,
        IQuery<String, Draft> {


}
