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
    Set<String> ACTIVITY_TYPES = new HashSet<String>() {{
        add("userTask");
        add("endEvent");
    }};

    /**
     * SCS
     */
    String SCS = "scs";

    /**
     * 自定义id前缀
     */
    String SID_FORMAT = "sid-%s";

    /**
     * 开始事件
     */
    String START_EVENT = "startEvent";

    /**
     * 结束事件
     */
    String END_EVENT = "endEvent";
    /**
     * 用户任务事件
     */
    String USER_TASK = "userTask";

    /**
     * 发起人事件
     */
    String INITIATOR_EVENT = "initiatorEvent";

    /**
     * 排他网关事件
     */
    String EXCLUSIVE_GATEWAY = "exclusiveGateway";

    /**
     * 并行网关事件
     */
    String PARALLEL_GATEWAY = "parallelGateway";

    /**
     * WebHook事件
     */
    String WEB_HOOK_EVENT = "webHookEvent";

    /**
     * SCS 消息产生事件
     */
    String SCS_BINDER_EVENT = "scsBinderEvent";
    /**
     * 邮件事件
     */
    String MAIL_EVENT = "mailEvent";

    /**
     * 作用在流程实例的开始事件
     */
    String EVENT_START = "start";

    /**
     * 作用在流程实例的结束事件
     */
    String EVENT_END = "end";

    /**
     * 作用在流程实例的连线上
     */
    String EVENT_TAKE = "take";

    /**
     * 节点类型
     */
    String NODE_RENDER_KEY = "renderKey";

    /**
     * 节点跳过表达式
     */
    String NODE_SKIP_EXPRESSION = "skipExpression";

    /**
     * 节点条件表达式
     */
    String NODE_CONDITION_EXPRESSION = "conditionExpression";

    /**
     * 节点条件表达式
     */
    String NODE_DYNAMIC_EXPRESSION = "dynamicExpression";

    /**
     * 节点标签
     */
    String NODE_EDGES = "edges";
    /**
     * 节点标签
     */
    String NODE_LABEL = "label";

    /**
     * 节点ID
     */
    String NODE_ID = "id";

    /**
     * 节点来源
     */
    String NODE_SOURCE = "source";

    /**
     * 节点目标
     */
    String NODE_TARGET = "target";

    /**
     * 节点用户类型
     */
    String NODE_USER_TYPE = "userType";

    /**
     * 节点数据类型
     */
    String NODE_DATA_TYPE = "dataType";

    /**
     * 节点自动完成
     */
    String NODE_AUTO_COMPLETE = "autoComplete";

    /**
     * 节点分配人
     */
    String NODE_ASSIGNEE = "assignee";
    /**
     * 节点动态
     */
    String NODE_DYNAMIC = "dynamic";

    /**
     * 节点候选人
     */
    String NODE_CANDIDATE_USERS = "candidateUsers";

    /**
     * 节点候选组
     */
    String NODE_CANDIDATE_GROUPS = "candidateGroups";
    /**
     * 节点Class
     */
    String NODE_CLASS = "class";

    /**
     * 节点请求方法
     */
    String NODE_REQUEST_METHOD = "requestMethod";

    /**
     * 节点请求URL
     */
    String NODE_REQUEST_URL = "requestUrl";

    /**
     * 节点请求头信息 如： Authentication: xxxx 多个换行
     */
    String NODE_REQUEST_CONTENT_TYPE = "contentType";

    String CONTENT_TYPE_APPLICATION_FORM = "application/x-www-form-urlencoded";

    /**
     * Content-Type
     */
    String CONTENT_TYPE = "Content-Type";

    /**
     * 头信息格式
     */
    String HEADER_FORMAT = "%s:%s";

    /**
     * 节点请求头信息 如： Authentication: xxxx 多个换行
     */
    String NODE_REQUEST_HEADERS = "requestHeaders";

    /**
     * 节点请求体
     */
    String NODE_REQUEST_BODY = "requestBody";

    /**
     * 节点请求超时时间
     */
    String NODE_REQUEST_TIMEOUT = "requestTimeout";

    /**
     * 节点请求失败状态码
     */
    String NODE_REQUEST_FAIL_CODES = "failStatusCodes";

    /**
     * 节点请求成功状态码
     */
    String NODE_REQUEST_HANDLE_CODES = "handleStatusCodes";

    /**
     * http监听器
     */
    String HTTP_TASK_LISTENER = "cn.meshed.cloud.workflow.domain.engine.listener.HttpTaskListener";

    /**
     * 节点cell
     */
    String CELL = "cell";

    /**
     * 节点发起人
     */
    String INITIATOR = "INITIATOR";
    /**
     * 节点发起人表达式
     */
    String INITIATOR_EL = "${INITIATOR}";
    /**
     * 为真表达式
     */
    String TRUE_EL = "${true}";

    /**
     * 字符串
     */
    String STRING = "String";

    /**
     * 表达式
     */
    String EXPRESSION = "expression";

    /**
     * 节点邮件收件人
     */
    String NODE_MAIL_TO = "to";

    /**
     * 节点邮件主体
     */
    String NODE_MAIL_SUBJECT = "subject";

    /**
     * 节点邮件抄送
     */
    String NODE_MAIL_CC = "cc";

    /**
     * 节点邮件密送
     */
    String NODE_MAIL_BCC = "bcc";

    /**
     * 节点邮件HTML值
     */
    String NODE_MAIL_HTML_VAR = "htmlVar";

    /**
     * 自定义属性SCS消息binding
     */
    String NODE_SCS_BINDING = "binding";
    /**
     * 自定义属性SCS消息binding
     */
    String NODE_SCS_NAMESPACE = "scs";

    /**
     * 自定义属性SCS消息body
     */
    String NODE_SCS_BODY = "body";

    /**
     * SCS Binder 监听器
     */
    String SCS_BINDER_DELEGATE = "cn.meshed.cloud.workflow.domain.engine.delegate.ScsBinderDelegate";

}
