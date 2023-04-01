package cn.meshed.cloud.workflow.flow.gatewayimpl;

import cn.meshed.cloud.utils.GzipUtils;
import cn.meshed.cloud.workflow.domain.flow.Designer;
import cn.meshed.cloud.workflow.domain.flow.gateway.DesignerGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * <h1>缓存设计图</h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@RequiredArgsConstructor
@Component("cacheDesignerGateway")
@Slf4j
public class CacheDesignerGateway implements DesignerGateway {

    public final StringRedisTemplate stringRedisTemplate;

    /**
     * 获取设计数据
     *
     * @param flowId 流程ID
     * @return 设计数据
     */
    @Override
    public String getDesigner(String flowId) {
        String redisKey = getKey(flowId);
        return GzipUtils.uncompress(stringRedisTemplate.opsForValue().get(redisKey));
    }

    /**
     * <h1>保存设计图信息</h1>
     *
     * @param designer 设计图信息
     * @return {@link Boolean}
     */
    @Override
    public Boolean save(Designer designer) {
        String redisKey = getKey(designer.getFlowId());
        stringRedisTemplate.opsForValue().set(redisKey, GzipUtils.compress(designer.getGraph()));
        return true;
    }

    /**
     * <h1>删除对象</h1>
     *
     * @param flowId Boolean
     * @return {@link Boolean}
     */
    @Override
    public Boolean delete(String flowId) {
        stringRedisTemplate.delete(getKey(flowId));
        return true;
    }

    /**
     * redis key
     *
     * @param flowId 流程ID
     * @return key
     */
    private String getKey(String flowId) {
        return String.format("Designer:DRAFT:%s", flowId);
    }
}
