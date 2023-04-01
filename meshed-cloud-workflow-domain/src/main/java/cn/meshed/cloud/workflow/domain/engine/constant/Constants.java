package cn.meshed.cloud.workflow.domain.engine.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
public interface Constants {

    /**
     * bpmn 后缀
     */
    String BPMN_FILE_SUFFIX = ".bpmn20.xml";

    /**
     * svg 后缀
     */
    String SVG_FILE_SUFFIX = ".svg";

    /**
     * 活动节点类型列表
     */
    Set<String> ACTIVITY_TYPES = new HashSet<String>(){{
        add("userTask");
        add("endEvent");
    }};
}
