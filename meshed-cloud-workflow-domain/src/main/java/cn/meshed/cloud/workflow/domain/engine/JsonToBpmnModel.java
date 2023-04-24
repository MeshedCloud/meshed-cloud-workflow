package cn.meshed.cloud.workflow.domain.engine;

import cn.hutool.json.JSONUtil;
import cn.meshed.cloud.utils.AssertUtils;
import cn.meshed.cloud.utils.IdUtils;
import com.alibaba.cola.exception.SysException;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.BpmnAutoLayout;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.EndEvent;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FieldExtension;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.ParallelGateway;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.bpmn.model.ServiceTask;
import org.flowable.bpmn.model.StartEvent;
import org.flowable.bpmn.model.UserTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.CELL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.CONTENT_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.CONTENT_TYPE_APPLICATION_FORM;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.END_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.EXCLUSIVE_GATEWAY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.HEADER_FORMAT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.INITIATOR;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.INITIATOR_EL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.INITIATOR_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.MAIL_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_ASSIGNEE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_AUTO_COMPLETE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CANDIDATE_GROUPS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CANDIDATE_USERS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CLASS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_CONDITION_EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_DATA_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_DYNAMIC;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_DYNAMIC_EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_EDGES;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_ID;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_LABEL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_BCC;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_CC;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_HTML_VAR;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_SUBJECT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_MAIL_TO;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_RENDER_KEY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_BODY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_CONTENT_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_FAIL_CODES;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_HANDLE_CODES;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_HEADERS;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_METHOD;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_TIMEOUT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_REQUEST_URL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_BINDING;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_BODY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SCS_NAMESPACE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SKIP_EXPRESSION;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_SOURCE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_TARGET;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.NODE_USER_TYPE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.PARALLEL_GATEWAY;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SCS_BINDER_DELEGATE;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SCS_BINDER_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.SID_FORMAT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.START_EVENT;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.TRUE_EL;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.USER_TASK;
import static cn.meshed.cloud.workflow.domain.engine.constant.Constants.WEB_HOOK_EVENT;

/**
 * <h1></h1>
 *
 * @author Vincent Vic
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = false)
@Slf4j
@Data
public class JsonToBpmnModel extends AbstractFlowElementHandle {

    private String name;
    private String key;
    private String json;

    public JsonToBpmnModel(String name, String key, String json) {
        this.name = name;
        this.key = key;
        this.json = json;
    }

    /**
     * 获取定义模型
     *
     * @return {@link BpmnModel}
     */
    public BpmnModel getBpmnModel() {
        AssertUtils.isTrue(StringUtils.isNotBlank(this.json), "模型注册必须存在数据库");
        //解析节点
        List<FlowElement> elements = parsingElements();
        AssertUtils.isTrue(CollectionUtils.isNotEmpty(elements), "设计不存在任何节点无效发布");
        //检测合法性
        validityVerification(elements);
        BpmnModel bpmnModel = new BpmnModel();
        bpmnModel.addProcess(getProcess(elements));
        return bpmnModel;
    }

    private Process getProcess(List<FlowElement> elements) {
        Process process = new Process();
        process.setId(this.key);
        process.setName(this.name);
        elements.forEach(process::addFlowElement);
        return process;
    }

    private void validityVerification(List<FlowElement> elements) {
        AssertUtils.isTrue(elements.size() >= 2, "最小节点至少有两个（开始-> 结束）");
        long startCount = elements.stream().filter(flowElement -> flowElement instanceof StartEvent).count();
        AssertUtils.isTrue(startCount == 1, "开始节点数仅支持一个");
        long endCount = elements.stream().filter(flowElement -> flowElement instanceof StartEvent).count();
        AssertUtils.isTrue(endCount == 1, "结束节点数仅支持一个");
    }

    private List<FlowElement> parsingElements() {
        Map<String, Object> map = JSONUtil.toBean(this.json, Map.class);
        if (map == null || map.size() <= 0) {
            throw new SysException("解析数据不存在");
        }

        //处理节点
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) map.get("nodes");
        List<FlowElement> elements = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(nodes)) {
            nodes.forEach(node -> {
                String nodeType = getNodeString(node, NODE_RENDER_KEY);
                //解析节点
                switch (Objects.requireNonNull(nodeType)) {
                    case START_EVENT:
                        elements.add(getStartEvent(node));
                        break;
                    case END_EVENT:
                        elements.add(getEndEvent(node));
                        break;
                    case USER_TASK:
                        elements.add(getUserTask(node));
                        break;
                    case INITIATOR_EVENT:
                        elements.add(getInitiatorEvent(node));
                        break;
                    case EXCLUSIVE_GATEWAY:
                        elements.add(getExclusiveGateway(node));
                        break;
                    case PARALLEL_GATEWAY:
                        elements.add(getParallelGateway(node));
                        break;
                    case MAIL_EVENT:
                        elements.add(getEmailEvent(node));
                        break;
                    case WEB_HOOK_EVENT:
                        elements.add(getWebHookEvent(node));
                        break;
                    case SCS_BINDER_EVENT:
                        elements.add(getScsBinderEvent(node));
                        break;
                    default:
                }
            });

        } else {
            throw new SysException("设计中不存在任何节点");
        }
        //处理边
        List<Map<String, Object>> edges = (List<Map<String, Object>>) map.get(NODE_EDGES);
        if (CollectionUtils.isNotEmpty(edges)) {
            edges.forEach(edge -> {
                elements.add(getSequenceFlow(edge));
            });
        } else {
            throw new SysException("设计中不存在任何连线");
        }
        return elements;
    }

    private ServiceTask getScsBinderEvent(Map<String, Object> node) {
        ServiceTask serviceTask = getServiceTask(node);
        serviceTask.setImplementationType(NODE_CLASS);
        serviceTask.setImplementation(SCS_BINDER_DELEGATE);
        addScsExtension(node, serviceTask);
        return serviceTask;
    }

    private void addScsExtension(Map<String, Object> node, ServiceTask serviceTask) {
        addExtensionElement(serviceTask, NODE_SCS_NAMESPACE, NODE_SCS_BINDING, getNodeString(node, NODE_SCS_BINDING));
        addExtensionElement(serviceTask, NODE_SCS_NAMESPACE, NODE_SCS_BODY, getNodeString(node, NODE_SCS_BODY));
    }

    /**
     * 获取邮件节点
     *
     * @param node 节点数据
     * @return {@link ServiceTask}
     */
    private ServiceTask getEmailEvent(Map<String, Object> node) {
        ServiceTask serviceTask = getServiceTask(node);
        //设置MAIL
        serviceTask.setType(ServiceTask.MAIL_TASK);
        serviceTask.setFieldExtensions(getEmailFieldExtensions(node));
        return serviceTask;
    }

    /**
     * 扩展信息
     *
     * @param node
     * @return
     */
    private List<FieldExtension> getEmailFieldExtensions(Map<String, Object> node) {
        List<FieldExtension> fieldExtensions = new ArrayList<>();
        addFieldString(fieldExtensions, node, NODE_MAIL_TO);
        addFieldString(fieldExtensions, node, NODE_MAIL_SUBJECT);
        addFieldString(fieldExtensions, node, NODE_MAIL_CC);
        addFieldString(fieldExtensions, node, NODE_MAIL_BCC);
        addFieldExpression(fieldExtensions, node, NODE_MAIL_HTML_VAR);
        return fieldExtensions;
    }

    /**
     * Web Hook 事件节点
     *
     * @param node
     * @return
     */
    private ServiceTask getWebHookEvent(Map<String, Object> node) {
        ServiceTask serviceTask = getServiceTask(node);
        //设置监听器
        // setListener(serviceTask);
        //设置HTTP
        serviceTask.setType(ServiceTask.HTTP_TASK);
        //设置HTTP信息
        serviceTask.setFieldExtensions(getHttpFieldExtensions(node));
        return serviceTask;
    }

    private List<FieldExtension> getHttpFieldExtensions(Map<String, Object> node) {
        List<FieldExtension> fieldExtensions = new ArrayList<>();
        addFieldString(fieldExtensions, node, NODE_REQUEST_METHOD);
        addFieldExpression(fieldExtensions, node, NODE_REQUEST_URL);
        addFieldExpression(fieldExtensions, node, NODE_REQUEST_BODY);
        addFieldString(fieldExtensions, node, NODE_REQUEST_TIMEOUT);
        addFieldString(fieldExtensions, node, NODE_REQUEST_FAIL_CODES);
        addFieldString(fieldExtensions, node, NODE_REQUEST_HANDLE_CODES);

        //headers 单独处理
        addFieldHeaders(fieldExtensions, node);

        return fieldExtensions;
    }

    /**
     * 添加头列表信息
     *
     * @param node            节点信息
     * @param fieldExtensions 自动扩展信息
     */
    private void addFieldHeaders(List<FieldExtension> fieldExtensions, Map<String, Object> node) {
        FieldExtension fieldExtension = new FieldExtension();
        String contentType = getNodeString(node, NODE_REQUEST_CONTENT_TYPE);
        if (StringUtils.isBlank(contentType)) {
            contentType = CONTENT_TYPE_APPLICATION_FORM;
        }

        String headers = String.format(HEADER_FORMAT, CONTENT_TYPE, contentType);
        String headerValues = getNodeString(node, NODE_REQUEST_HEADERS);
        if (StringUtils.isNotBlank(headerValues)) {
            headers = headers + "\n" + headerValues;
        }
        fieldExtension.setFieldName(NODE_REQUEST_HEADERS);
        fieldExtension.setExpression(headers);
        fieldExtensions.add(fieldExtension);
    }


    private SequenceFlow getSequenceFlow(Map<String, Object> edge) {
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId(String.format(SID_FORMAT, IdUtils.randomUUID()));
        sequenceFlow.setSourceRef(getMapInnerValue(edge, NODE_SOURCE, CELL));
        sequenceFlow.setTargetRef(getMapInnerValue(edge, NODE_TARGET, CELL));
        sequenceFlow.setConditionExpression(getNodeString(edge, NODE_CONDITION_EXPRESSION));
        AssertUtils.isTrue(StringUtils.isNotBlank(sequenceFlow.getSourceRef()), "流程连线缺少数据");
        AssertUtils.isTrue(StringUtils.isNotBlank(sequenceFlow.getTargetRef()), "流程连线缺少数据");
        return sequenceFlow;
    }

    private String getMapInnerValue(Map<String, Object> edge, String key, String innerKey) {
        Map<String, Object> source = (Map<String, Object>) edge.get(key);
        if (source != null) {
            return (String) source.get(innerKey);
        }
        return null;
    }

    /**
     * 得到发起人事件节点模型
     * 本质是用户节点
     *
     * @param node 节点原始数据
     * @return
     */
    private UserTask getInitiatorEvent(Map<String, Object> node) {
        UserTask userTask = new UserTask();
        userTask.setId(getNodeString(node, NODE_ID));
        userTask.setName(getNodeString(node, NODE_LABEL));
        userTask.setAssignee(INITIATOR_EL);
        //todo 后期调整为监听完成任务
        Boolean autoComplete = (Boolean) node.get(NODE_AUTO_COMPLETE);
        if (autoComplete) {
            userTask.setSkipExpression(TRUE_EL);
        }
        return userTask;
    }

    private UserTask getUserTask(Map<String, Object> node) {
        UserTask userTask = new UserTask();
        userTask.setId(getNodeString(node, NODE_ID));
        userTask.setName(getNodeString(node, NODE_LABEL));
        String userType = getNodeString(node, NODE_USER_TYPE);
        String dataType = getNodeString(node, NODE_DATA_TYPE);

        if (NODE_ASSIGNEE.equals(userType)) {
            if (NODE_DYNAMIC.equals(dataType)) {
                userTask.setAssignee(getNodeString(node, NODE_DYNAMIC_EXPRESSION));
            } else {
                userTask.setAssignee(getNodeString(node, NODE_ASSIGNEE));
            }
        } else if (NODE_CANDIDATE_USERS.equals(userType)) {
            if (NODE_DYNAMIC.equals(dataType)) {
                userTask.setCandidateUsers(Collections.singletonList(getNodeString(node, NODE_DYNAMIC_EXPRESSION)));
            } else {
                userTask.setCandidateUsers(getNodeList(node, NODE_CANDIDATE_USERS));
            }

        } else if (NODE_CANDIDATE_GROUPS.equals(userType)) {
            if (NODE_DYNAMIC.equals(dataType)) {
                userTask.setCandidateGroups(Collections.singletonList(getNodeString(node, NODE_DYNAMIC_EXPRESSION)));
            } else {
                userTask.setCandidateGroups(getNodeList(node, NODE_CANDIDATE_GROUPS));
            }
        }
        userTask.setSkipExpression(getNodeString(node, NODE_SKIP_EXPRESSION));
        return userTask;
    }

    private EndEvent getEndEvent(Map<String, Object> node) {
        EndEvent endEvent = new EndEvent();
        endEvent.setId(getNodeString(node, NODE_ID));
        endEvent.setName(getNodeString(node, NODE_LABEL));
        return endEvent;
    }

    private ExclusiveGateway getExclusiveGateway(Map<String, Object> node) {
        ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
        exclusiveGateway.setId(getNodeString(node, NODE_ID));
        exclusiveGateway.setName(getNodeString(node, NODE_LABEL));
        return exclusiveGateway;
    }

    private ParallelGateway getParallelGateway(Map<String, Object> node) {
        ParallelGateway parallelGateway = new ParallelGateway();
        parallelGateway.setId(getNodeString(node, NODE_ID));
        parallelGateway.setName(getNodeString(node, NODE_LABEL));
        return parallelGateway;
    }

    private StartEvent getStartEvent(Map<String, Object> node) {
        StartEvent startEvent = new StartEvent();
        startEvent.setFormKey(getNodeString(node, "formKey"));
        startEvent.setId(getNodeString(node, NODE_ID));
        startEvent.setInitiator(INITIATOR);
        startEvent.setName(getNodeString(node, NODE_LABEL));
        return startEvent;
    }


}
