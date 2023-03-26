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

import java.time.LocalDate;
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

    public final StringRedisTemplate stringRedisTemplate;
    private final String DRAFT_REDIS_KEY = "DRAFT:%s";
    private final String DRAFT_LIST_REDIS_KEY = "DRAFT_LIST";

    /**
     * <h1>保存对象</h1>
     *
     * @param draft 草稿数据
     * @return {@link Boolean}
     */
    @Override
    public Boolean save(Draft draft) {
        //新增日期
        draft.setCreateDate(LocalDate.now());
        stringRedisTemplate.opsForList().leftPush(DRAFT_LIST_REDIS_KEY, draft.getId());
        stringRedisTemplate.opsForValue().set(getRedisKey(draft.getId()), JSONUtil.toJsonStr(draft));
        return true;
    }

    private String getRedisKey(String uuid) {
        return String.format(DRAFT_REDIS_KEY, uuid);
    }

    /**
     * <h1>搜索列表</h1>
     *
     * @param pageQry
     * @return {@link PageResponse<Draft>}
     */
    @Override
    public PageResponse<Draft> searchList(DraftPageQry pageQry) {
        Long size = stringRedisTemplate.opsForList().size(DRAFT_LIST_REDIS_KEY);
        if (size == null || size == 0){
            return PageResponse.of(pageQry.getPageSize(), pageQry.getPageIndex());
        }
        List<String> list = stringRedisTemplate.opsForList().range(DRAFT_LIST_REDIS_KEY,
                pageQry.getOffset(), pageQry.getOffset() + pageQry.getPageSize());
        if (CollectionUtils.isEmpty(list)) {
            return PageResponse.of(pageQry.getPageSize(), pageQry.getPageIndex());
        }
        list = list.stream().map(s -> getRedisKey(s)).collect(Collectors.toList());
        //获取到实际数据
        List<String> jsonDatas = stringRedisTemplate.opsForValue().multiGet(list);
        if (CollectionUtils.isEmpty(jsonDatas)) {
            log.warn("分页数据丢失： {}", JSONUtil.toJsonStr(pageQry));
            return PageResponse.buildFailure("404", "分页数据丢失");
        }
        List<Draft> drafts = jsonDatas.stream().map(s -> JSONUtil.toBean(s, Draft.class)).collect(Collectors.toList());
        return PageResponse.of(drafts, Math.toIntExact(size), pageQry.getPageSize(), pageQry.getPageIndex());
    }

    /**
     * 查询
     *
     * @param id ID
     * @return {@link Draft}
     */
    @Override
    public Draft query(String id) {
        String json = stringRedisTemplate.opsForValue().get(getRedisKey(id));
        return JSONUtil.toBean(json, Draft.class);
    }
}
