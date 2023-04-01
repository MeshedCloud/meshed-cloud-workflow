package cn.meshed.cloud.workflow.flow.gatewayimpl;

import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.CopyUtils;
import cn.meshed.cloud.utils.IdUtils;
import cn.meshed.cloud.utils.PageUtils;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.gatewayimpl.database.dataobject.FlowDraftDO;
import cn.meshed.cloud.workflow.flow.gatewayimpl.database.mapper.FlowDraftMapper;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static cn.meshed.cloud.workflow.domain.flow.constant.Constants.DRAFT_PREFIX;

/**
 * <h1>缓存草稿箱</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class DefaultDraftGateway implements DraftGateway {

    public final FlowDraftMapper flowDraftMapper;

    /**
     * <h1>保存对象</h1>
     *
     * @param draft 草稿数据
     * @return {@link Boolean}
     */
    @Override
    public String save(Draft draft) {
        //新增日期
        FlowDraftDO draftDO = CopyUtils.copy(draft, FlowDraftDO.class);
        draftDO.setId(DRAFT_PREFIX+ IdUtils.simpleUUID());
        AssertUtils.isTrue(flowDraftMapper.insert(draftDO) > 0,"新增草稿失败");
        return draftDO.getId();
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry
     * @return {@link PageResponse<Draft>}
     */
    @Override
    public PageResponse<Draft> searchList(DraftPageQry pageQry) {
        Page<Object> page = PageUtils.startPage(pageQry);
        LambdaQueryWrapper<FlowDraftDO> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(FlowDraftDO::getCreateTime);
        return PageUtils.of(flowDraftMapper.selectList(lqw), page, Draft::new);
    }

    /**
     * 查询
     *
     * @param id ID
     * @return {@link Draft}
     */
    @Override
    public Draft query(String id) {
        return CopyUtils.copy(flowDraftMapper.selectById(id),Draft.class);
    }

    /**
     * <h1>删除对象</h1>
     *
     * @param id id
     * @return {@link Boolean}
     */
    @Override
    public Boolean delete(String id) {
        return flowDraftMapper.deleteById(id) > 0;
    }
}
