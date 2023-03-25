package cn.meshed.cloud.workflow.flow.gatewayimpl;

import cn.hutool.json.JSONUtil;
import cn.meshed.cloud.workflow.domain.flow.Draft;
import cn.meshed.cloud.workflow.domain.flow.gateway.DraftGateway;
import cn.meshed.cloud.workflow.flow.query.DraftPageQry;
import com.alibaba.cola.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <h1>缓存草稿箱</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component("cacheDraftGateway")
@Slf4j
public class CacheDraftGateway implements DraftGateway {

    public StringRedisTemplate stringRedisTemplate;
    private final String DRAFT_REDIS_KEY = "DRAFT_LIST";

    /**
     * <h1>保存对象</h1>
     *
     * @param draft 草稿数据
     * @return {@link Boolean}
     */
    @Override
    public Boolean save(Draft draft) {
        stringRedisTemplate.opsForList().leftPush(DRAFT_REDIS_KEY, JSONUtil.toJsonStr(draft));
        return true;
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry
     * @return {@link PageResponse<Draft>}
     */
    @Override
    public PageResponse<Draft> searchList(DraftPageQry pageQry) {
        Long size = stringRedisTemplate.opsForList().size(DRAFT_REDIS_KEY);
        List<String> list = stringRedisTemplate.opsForList().range(DRAFT_REDIS_KEY,
                pageQry.getOffset(), pageQry.getOffset() + pageQry.getPageSize());
        if (CollectionUtils.isEmpty(list)) {
            return PageResponse.of(pageQry.getPageSize(), pageQry.getPageIndex());
        }
        List<Draft> drafts = list.stream().map(s -> JSONUtil.toBean(s, Draft.class)).collect(Collectors.toList());
        return PageResponse.of(drafts, Math.toIntExact(size), pageQry.getPageSize(), pageQry.getPageIndex());
    }
}
